package com.npixel.nodelibrary.color;

import com.npixel.base.bitmap.Bitmap;
import com.npixel.base.bitmap.Color;
import com.npixel.base.node.Node;
import com.npixel.base.node.NodeSocket;
import com.npixel.base.node.NodeSocketType;
import com.npixel.base.properties.DoubleProperty;
import com.npixel.base.properties.PropertyGroup;
import com.npixel.base.tree.NodeTree;
import com.npixel.gui.icons.Icons;

public class ColorAlphaCompositeNode extends Node {
    public ColorAlphaCompositeNode(NodeTree tree) {
        super(tree);

        typeString = "ColorAComp";
        icon = Icons.getIcon("coloracomp");

        propertyGroups.add(new PropertyGroup(
                "acomp", "Composite",
                new DoubleProperty(this, "opacity", "Foreground opacity", 1, 0, 1)
        ));

        inputs.add(new NodeSocket(this, "a", NodeSocketType.INPUT, "Background", new Bitmap(1, 1)));
        inputs.add(new NodeSocket(this, "b", NodeSocketType.INPUT, "Foreground", new Bitmap(1, 1)));
        outputs.add(new NodeSocket(this, "out", NodeSocketType.OUTPUT, "Output", new Bitmap(1, 1)));
    }

    @Override
    public void process() {
        Bitmap outBitmap = Bitmap.createEncompassing((Bitmap)getInputValue("a"), (Bitmap)getInputValue("b"));
        getOutput("out").setValue(outBitmap);

        Bitmap bmpA = (Bitmap)getInputValue("a");
        Bitmap bmpB = (Bitmap)getInputValue("b");

        double opacityValue = ((DoubleProperty)getProperty("acomp", "opacity")).getValue();

        outBitmap.scan((x, y, color) -> {
            Color fg = bmpB.getPixel(x, y);
            fg.setAlpha(fg.getAlpha() * opacityValue);

            return Color.over(bmpA.getPixel(x, y), fg);
        });

        super.process();
    }

    @Override
    public Bitmap getThumbnail() {
        return (Bitmap)getOutput("out").getValue();
    }
}
