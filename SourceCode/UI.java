import java.util.ArrayList;
import java.util.List;

import javafx.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.control.*;
import javafx.scene.shape.*;

public class UI extends Application {
    public static void main(String[] args) {
        launch();
    }

    Rectangle rbase = new Rectangle(400, 100, Color.SADDLEBROWN);
    Rectangle rbarA = new Rectangle(10, 100, Color.BROWN);
    Rectangle rbarB = new Rectangle(10, 100, Color.BROWN);
    Rectangle rbarC = new Rectangle(10, 100, Color.BROWN);
    Button btnSolve = new Button("reset and solve");
    Button btnGenerate = new Button("generate Blocks");
    Button btnFirsttoSecond = new Button("1 --> 2");
    Button btnFirsttoThird = new Button("1 --> 3");
    Button btnSecondtoFirst = new Button("2 --> 1");
    Button btnSecondtoThird = new Button("2 --> 3");
    Button btnThirdtoFirst = new Button("3 --> 1");
    Button btnThirdtoSecond = new Button("3 --> 2");
    TextField tf = new TextField();
    List<Rectangle> rectangles;
    @Override
    public void start(Stage s) throws Exception {
        
        makeGUI(s);
    }
    private void generateRectangles(){
        int n= Integer.parseInt(tf.getText());
        rectangles = new ArrayList<>();
        double startWidth = 100;
        double startHeight= 50;
        double startXRect=146;
        double startYRect=640;
        for(int i=0;i<n;i++){
            Rectangle r = new Rectangle(startXRect, startYRect, startWidth, startHeight);
            double tmpWidth= startWidth;
            startWidth*=0.85;
            startHeight*=0.85;
            startYRect -=startHeight;
            startXRect += (tmpWidth-startWidth)/2;
            rectangles.add(r);             
        }
        
        for(Rectangle r : rectangles){
    
            p.getChildren().add(r);

        }
    }
    private void makeGUI(Stage s){
        HBox vb = new HBox();
        vb.getChildren().add(tf);
        tf.setMaxWidth(40);
        vb.getChildren().add(btnGenerate);
        vb.getChildren().add(btnSolve);
        vb.getChildren().add(btnFirsttoSecond);        
        vb.getChildren().add(btnFirsttoThird);
        vb.getChildren().add(btnSecondtoFirst);
        vb.getChildren().add(btnSecondtoThird);
        vb.getChildren().add(btnThirdtoFirst);
        vb.getChildren().add(btnThirdtoSecond);
        btnGenerate.setOnAction(e->generateRectangles());
        btnSolve.setOnAction(e->reset());
        rbase.widthProperty().bind(s.widthProperty());
        rbase.yProperty().bind(s.heightProperty().subtract(rbase.getHeight()));

        rbarA.heightProperty().bind(s.heightProperty().divide(5).multiply(4));
        rbarA.yProperty().bind(s.heightProperty().divide(20).multiply(2));
        rbarA.xProperty().bind(s.widthProperty().divide(2).divide(2));

        rbarB.heightProperty().bind(s.heightProperty().divide(5).multiply(4));
        rbarB.yProperty().bind(s.heightProperty().divide(20).multiply(2));
        rbarB.xProperty().bind(s.widthProperty().divide(2));

        rbarC.heightProperty().bind(s.heightProperty().divide(5).multiply(4));
        rbarC.yProperty().bind(s.heightProperty().divide(20).multiply(2));
        rbarC.xProperty().bind(s.widthProperty().subtract(rbarA.xProperty()));

        
        p = new Pane();
        p.getChildren().addAll(rbarA, rbarB, rbarC, rbase, vb);
        sc = new Scene(p, 750, 750);

        s.setScene(sc);
        s.setMinWidth(200);
        s.setMinHeight(400);
        s.setTitle("Towers of Hanoi");
        s.show();
    }
    private Pane p;
    private Scene sc;
    private void reset(){
        for(Rectangle r : rectangles){
            r.setVisible(false);
        }
    }
}
