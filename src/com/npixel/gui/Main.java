package com.npixel.gui;

import com.npixel.base.Document;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.tree.NodeTree;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class Main extends Application {
    private static class TestNode extends Node {
        TestNode(NodeTree tree, String name) {
            super(tree);

            typeString = "Test";
            this.name = name;

            inputs.add(new NodeSocket(this, "a", NodeSocketType.INPUT, "Number A", 2));
            inputs.add(new NodeSocket(this, "b", NodeSocketType.INPUT, "Number B", 2));
            outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Output", 0, "a"));
        }

        @Override
        public void process() {
            getOutput("out").setValue((Integer)getInputValue("a") + (Integer)getInputValue("b"));
            super.process();
        }
    }

    private void createTestNodeTree(NodeTree tree) {
        TestNode n1 = new TestNode(tree, "Test1");
        n1.setX(30);
        n1.setY(90);
        tree.addNode(n1);
        TestNode n2 = new TestNode(tree, "Test2");
        n2.setX(200);
        n2.setY(30);
        tree.addNode(n2);
        TestNode n3 = new TestNode(tree, "Test3");
        n3.setX(200);
        n3.setY(150);
        tree.addNode(n3);
        TestNode n4 = new TestNode(tree, "Test4");
        n4.setX(400);
        n4.setY(40);
        tree.addNode(n4);

        tree.connect(n1, "out", n2, "a");
        tree.connect(n1, "out", n3, "a");
        tree.connect(n2, "out", n4, "a");
        tree.connect(n3, "out", n4, "b");
    }

    @Override
    public void start(Stage primaryStage) {
        HBox root = new HBox();

        Document doc = new Document(300, 200);
        createTestNodeTree(doc.getTree());

        DocumentView docView = new DocumentView(doc);
        root.getChildren().add(docView);

        HBox.setHgrow(docView, Priority.ALWAYS);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1140, 768));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
