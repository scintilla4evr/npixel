package com.npixel.gui.sidepanel;

import com.npixel.base.Document;
import com.npixel.base.palette.NamedColor;
import com.npixel.base.palette.Palette;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javax.print.Doc;

public class PalettePanel extends VBox {
    private final Document doc;
    private TilePane tilePane;

    public PalettePanel(Document doc) {
        this.doc = doc;

        prepareLayout();
    }

    private void prepareLayout() {
        ComboBox<Palette> palettePicker = new ComboBox<>(doc.getPalettes());
        palettePicker.setValue(doc.getPalettes().get(0));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setMaxHeight(200);
        scrollPane.setFitToWidth(true);

        tilePane = new TilePane();

        updatePane(palettePicker.getValue());
        palettePicker.setOnAction(event -> updatePane(palettePicker.getValue()));

        scrollPane.setContent(tilePane);

        getChildren().addAll(palettePicker, scrollPane);
    }

    private void updatePane(Palette palette) {
        tilePane.getChildren().clear();

        for (NamedColor color : palette.getColors()) {
            tilePane.getChildren().add(new ColorSwatchNode(doc, color));
        }
    }

    private static class ColorSwatchNode extends Canvas {
        private final ContextMenu contextMenu;

        public ColorSwatchNode(Document doc, NamedColor color) {
            setWidth(16);
            setHeight(16);

            fillColor(color);

            contextMenu = new ContextMenu();
            prepareContextMenu(doc, color);

            setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    doc.setForegroundColor(color);
                } else {
                    contextMenu.show(this, event.getScreenX(), event.getScreenY());
                }
            });
        }

        private void prepareContextMenu(Document doc, NamedColor color) {
            MenuItem colorName = new MenuItem(color.getName());
            colorName.setDisable(true);

            MenuItem setAsFg = new MenuItem("Set as foreground");
            setAsFg.setOnAction(event -> doc.setForegroundColor(color));

            MenuItem setAsBg = new MenuItem("Set as background");
            setAsBg.setOnAction(event -> doc.setBackgroundColor(color));

            contextMenu.getItems().addAll(
                    colorName,
                    setAsFg, setAsBg
            );
        }

        private void fillColor(NamedColor color) {
            GraphicsContext ctx = getGraphicsContext2D();

            ctx.setFill(Color.BLACK);
            ctx.fillRect(0, 0, 16, 16);
            ctx.setFill(color.getFXColor());
            ctx.fillRect(1, 1, 14, 14);
        }
    }
}
