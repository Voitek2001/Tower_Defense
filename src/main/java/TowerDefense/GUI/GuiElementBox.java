package TowerDefense.GUI;

import TowerDefense.WorldMapElements.IMapElement;
import TowerDefense.WorldMapElements.Tower;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;

public class GuiElementBox {

    private VBox guiElement = new VBox(4);
    public GuiElementBox(IMapElement mapElement, String parentPath, int imgViewWidth, int imgViewHeight) {
        try {
            Image image = ImageLoader.loadImage(parentPath + "/" + mapElement.getPathToImage());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(imgViewWidth);
            imageView.setFitHeight(imgViewHeight);
//            System.out.println(imgViewHeight);
//            guiElement.getChildren().add(imageView);
            if (!(mapElement instanceof Tower)) { // consider creating new interface for towers
                ProgressBar barHP = new ProgressBar();
                barHP.setPrefSize(200, 15);
                barHP.setProgress((double) mapElement.getHP() / mapElement.getMaxHP());
                guiElement = new VBox(imageView, barHP);

            } else {
                guiElement = new VBox(imageView);
            }
            guiElement.setAlignment(Pos.CENTER);


//            VBox.setMargin(imageView, new Insets(0, 0, 0, 0));


        }
        catch (FileNotFoundException e) {
            e.printStackTrace(System.out);
        }
    }

    public VBox getGUIElement() {
        return guiElement;
    }



}
