package org.example.windows_exe;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.ProxySelector;
import java.util.prefs.Preferences;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        BorderPane root = new BorderPane();

        // 使用类加载器加载图片资源
        // 加载背景图片
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("bg.jpg");
        if (inputStream != null) {
            Image image = new Image(inputStream);
            BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
            BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null, backgroundSize);
            Background background = new Background(backgroundImage);
            root.setBackground(background);
        } else {
            System.err.println("无法加载背景图片: bg.jpg");
        }

        // 创建菜单
        Menu fileMenu = new Menu("File");
        // File里面的元素
        MenuItem newMenuItem = new MenuItem("New");
        MenuItem openMenuItem = new MenuItem("Open");
        MenuItem saveMenuItem = new MenuItem("Save");
        MenuItem exitMenuItem = new MenuItem("Exit");
        fileMenu.getItems().addAll(newMenuItem, openMenuItem, saveMenuItem, exitMenuItem);

        // 同理
        Menu helpMenu = new Menu("About");

        // 创建菜单栏
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, helpMenu);

        // 将菜单栏添加到顶部
        root.setTop(menuBar);

        // 添加两个输入框
        // 使用VBox布局容器来垂直排列输入框
        VBox vBox = new VBox(); // 使用 VBox 垂直布局容器
        vBox.setSpacing(10); // 设置垂直间距为 10 像素
        vBox.setPadding(new Insets(10)); // 设置容器的内边距为 10 像素

        Label label1 = new Label("Host:");
        Label label2 = new Label("Port:");
        label1.setTextFill(Color.WHITE);
        label2.setTextFill(Color.WHITE);

        TextField textField1 = new TextField();
        TextField textField2 = new TextField();

        HBox inputBox1 = new HBox(10, label1, textField1); // 使用 HBox 横向布局容器将 Label 和 TextField 放在一起
        inputBox1.setPadding(new Insets(10)); // 设置 inputBox1 的内边距为 5 像素
        HBox inputBox2 = new HBox(10, label2, textField2);
        inputBox2.setPadding(new Insets(10)); // 设置 inputBox2 的内边距为 5 像素

        vBox.getChildren().addAll(inputBox1, inputBox2); // 将两个输入框添加到 VBox 中

        root.setLeft(vBox); // 将 VBox 添加到 BorderPane 的左侧

        // 添加按钮
        Button modifyButton = new Button("修改");
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(modifyButton);
        stackPane.setAlignment(modifyButton, javafx.geometry.Pos.BOTTOM_RIGHT);
        root.setBottom(stackPane);

        // 设置按钮点击事件处理程序
        modifyButton.setOnAction(event -> {
            String host = textField1.getText();
            String port = textField2.getText();
            if (!host.isEmpty() && !port.isEmpty()) {
                // 设置系统的HTTP代理
                System.setProperty("http.proxyHost", host);
                System.setProperty("http.proxyPort", port);

                // 重新加载代理选择器
                ProxySelector.setDefault(ProxySelector.getDefault());

                System.out.println("已修改系统的HTTP代理为 Host: " + host + ", Port: " + port);
            } else {
                System.out.println("请输入有效的 Host 和 Port");
            }
        });

        // 窗口定义
        Scene scene = new Scene(root, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Http-Proxy Tools");
        primaryStage.show();
    }

    public static void main(String[] args) {
        System.setProperty("java.net.useSystemProxies", "true"); // 添加这行
        launch();
    }

    // 设置系统代理的方法
    public static void setProxy(String host, String port) {
        try {
            Preferences pref = Preferences.userRoot().node("Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings");
            pref.put("ProxyEnable", "1"); // 启用代理
            pref.put("ProxyServer", "http://" + host + ":" + port); // 设置代理服务器地址和端口
            System.out.println("已修改系统的HTTP代理为 Host: " + host + ", Port: " + port);
        } catch (Exception e) {
            System.err.println("无法设置HTTP代理：" + e.getMessage());
        }
    }
}