import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import javafx.animation.*;
import javafx.*;
import javafx.event.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.control.*;
import javafx.scene.shape.*;
import javafx.util.Duration;

public class TowersOfHanoi extends Application {
    public static void main(String[] args) {
        launch();
    }
    
    //Graphical Elements:
    Rectangle rbase = new Rectangle(400, 100, Color.SADDLEBROWN);
    Rectangle rbarA = new Rectangle(10, 100, Color.BROWN);
    Rectangle rbarB = new Rectangle(10, 100, Color.BROWN);
    Rectangle rbarC = new Rectangle(10, 100, Color.BROWN);
    ArrayList<Rectangle> bars = new ArrayList<Rectangle>();
    Button btnSolve = new Button("solve");
    Button btnSolveByStep = new Button("solve by step");
    Button btnGenerate = new Button("generate Blocks");
    Button btnTarget = new Button("Target:2");
    Button btnFirsttoSecond = new Button("1 --> 2");
    Button btnFirsttoThird = new Button("1 --> 3");
    Button btnSecondtoFirst = new Button("2 --> 1");
    Button btnSecondtoThird = new Button("2 --> 3");
    Button btnThirdtoFirst = new Button("3 --> 1");
    Button btnThirdtoSecond = new Button("3 --> 2");
    TextField tf = new TextField();
    List<Rectangle> rectangles;
    
    //Logical Elements:
    ArrayList<Integer> stack1 = new ArrayList<>();
    ArrayList<Integer> stack2 = new ArrayList<>();
    ArrayList<Integer> stack3 = new ArrayList<>();
    ArrayList<ArrayList<Integer>> stacks = new ArrayList<ArrayList<Integer>>();
    
    
    @Override
    public void start(Stage s) throws Exception {
    	target = 1;
    	solCount = 0;				//only when using PauseTransition
    	moves = new ArrayList<Move>();
    	solution = new ArrayList<Move>();
    	bars.add(rbarA);
        bars.add(rbarB);
        bars.add(rbarC);
        stacks.add(stack1);
        stacks.add(stack2);
        stacks.add(stack3);
        makeGUI(s);
        
    }
    private void generateRectangles(Stage s){
        int n= Integer.parseInt(tf.getText());
        rectangles = new ArrayList<>();
        double deltaWidth = 0;
        double startWidth = 500;
        double startHeight= 50;
        double startXRect=146;
        double startYRect=640;
        for(int i=0;i<n;i++){
            Rectangle r = new Rectangle(startXRect, startYRect, startWidth, startHeight);
            r.setFill(rndmColor());
            r.minWidth(100);
            r.widthProperty().bind(s.widthProperty().divide(4).subtract(deltaWidth));
           // r.heightProperty().bind(s.heightProperty().divide(n+2));	//line to allow height to be relative to screen height
            deltaWidth += 25;
            startYRect -=startHeight;
            r.xProperty().bind(rbarA.xProperty().subtract(r.widthProperty().divide(2).subtract(0.5*rbarA.getWidth())));
            r.yProperty().bind(rbase.yProperty().subtract((rectangles.size())*r.getHeight()+r.getHeight()));
            rectangles.add(r);             
        }
        
        for(Rectangle r : rectangles){
    
            p.getChildren().add(r);

        }
        
        //Update Stack-Data (adding all Rectangles to first Stack:
        for(int i = 0; i < rectangles.size(); ++i)
        {
        	stack1.add(i);
        }
        
        //Prepare solution based on current (final) target-Stack:
        if(target == 1)
        	solve(rectangles.size(), 0, 1, 2, solution);
        else if(target == 2)
        	solve(rectangles.size(), 0, 2, 1, solution);
        
        for(int i = 0; i < solution.size(); ++i)
        {
        	System.out.println("Move Disc from " + solution.get(i).from + " to " + solution.get(i).to + ".");
        }
        
        
    }
    private void makeGUI(Stage s){
        HBox vb = new HBox();
        vb.getChildren().add(tf);
        tf.setMaxWidth(40);
        vb.getChildren().add(btnGenerate);
        vb.getChildren().add(btnSolve);
        vb.getChildren().add(btnSolveByStep);
        vb.getChildren().add(btnTarget);
        vb.getChildren().add(btnFirsttoSecond);        
        vb.getChildren().add(btnFirsttoThird);
        vb.getChildren().add(btnSecondtoFirst);
        vb.getChildren().add(btnSecondtoThird);
        vb.getChildren().add(btnThirdtoFirst);
        vb.getChildren().add(btnThirdtoSecond);
        //Logic:
        btnFirsttoSecond.setOnAction(e -> move(stack1, 0, stack2, 1));
        btnFirsttoThird.setOnAction(e -> move(stack1, 0, stack3, 2));
        btnSecondtoFirst.setOnAction(e -> move(stack2, 1, stack1, 0));
        btnSecondtoThird.setOnAction(e -> move(stack2, 1, stack3, 2));
        btnThirdtoFirst.setOnAction(e -> move(stack3, 2, stack1, 0));
        btnThirdtoSecond.setOnAction(e -> move(stack3, 2, stack2, 1));
        
        btnGenerate.setOnAction(e->generateRectangles(s));
        btnSolve.setOnAction(e->autoSolve());
        btnSolveByStep.setOnAction(e -> solveByStep());
        btnTarget.setOnAction(e -> {					//!only effective if used before start of game (Generation of rectangles)
        	if(btnTarget.getText().equals("Target:2"))
        	{
        		btnTarget.setText("Target:3");
        		target = 2;		//Stack with index 2 is target
        	}
        });
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
    
    //Automatically rewind to last appropriate state and solve problem
    private void autoSolve()
    {
    	
    	
    	//Undo incorrect steps:
    	while(moves.size() > 0 && !moves.get(moves.size()-1).correct)
    	{
    		Move m = moves.get(moves.size()-1);
    		move(stacks.get(m.to), m.to, stacks.get(m.from), m.from);	//Undo incorrect moves
    		moves.remove(moves.size()-1);		//Remove undone move from list
    		
    		//wait:
    		try {TimeUnit.SECONDS.sleep(2);} catch(InterruptedException e) {System.out.println("Problem");}
    	}
    	
    	//Do remaining correct steps:
    	int i = moves.size();
    	while(i < solution.size())
    	{
    		Move m = solution.get(i);
    		move(stacks.get(m.from), m.from, stacks.get(m.to), m.to);	//Make one step in correct direction
    		++i;
    		
    		
    		try {TimeUnit.SECONDS.sleep(2);} catch(InterruptedException e) {System.out.println("Problem");}
    		
    	}
    	
    	
    	//undoSteps();		//only when using PauseTransition; doesn't work properly
    	/*
    	solveByStep();
    	if(!moves.get(moves.size()-1).correct || moves.size() < solution.size())
    	{
    		System.out.println("Loop");
    	}
    	*/
    	
    }
    
    private void undoSteps()	//only when using PauseTransition; doesn't work properly
    {
    	if(moves.size() > 0 && !moves.get(moves.size()-1).correct)
		{
	    	Move m = moves.get(moves.size()-1);
			move(stacks.get(m.to), m.to, stacks.get(m.from), m.from);	//Undo incorrect moves
			moves.remove(moves.size()-1);		//Remove undone move from list
			
		}
    	//solCount = moves.size();
		finish();
    }
    
    private void finish()	//only when using PauseTransition; doesn't work properly
    {
    	Move m = solution.get(solCount);
		move(stacks.get(m.from), m.from, stacks.get(m.to), m.to);	//Make one step in correct direction
		++solCount;
    }
    
    private void solveByStep()
    {
    	//Undo incorrect step:
    	if(moves.size() > 0 && !moves.get(moves.size()-1).correct)
    	{
    		Move m = moves.get(moves.size()-1);
    		move(stacks.get(m.to), m.to, stacks.get(m.from), m.from);	//Undo incorrect moves
    		moves.remove(moves.size()-1);		//Remove undone move from list
    		
    	}
    	
    	//Do remaining correct step:
    	int i = moves.size();
    	if(i < solution.size())
    	{
    		Move m = solution.get(i);
    		move(stacks.get(m.from), m.from, stacks.get(m.to), m.to);	//Make one step in correct direction
    		
    	}
    }
    
    
    private void move(ArrayList<Integer> from, int fromNumber, ArrayList<Integer> to, int toNumber)
    {
    	if(from.size() > 0)		//Do nothing if Stack is empty
    	{
    		if(to.size() == 0 || from.get(from.size()-1) > to.get(to.size()-1))	//Do nothing if disc in from is larger than disc in to
    		{
    			//If this is the first move:
    			if(moves.size() == 0)	
    			{
    				if(isSame(solution.get(0), new Move(fromNumber, toNumber, true)))
    				{
    					moves.add(new Move(fromNumber, toNumber, true));
    				}
    				else
    				{
    					moves.add(new Move(fromNumber, toNumber, false));
    				}
    			}
    			//check if previous move was undone
    			else if(isOpposite(moves.get(moves.size()-1), new Move(fromNumber, toNumber, true)))
    			{
    				moves.remove(moves.size()-1);	//undid previous move; => delete that move instead of adding new one
    			}
    			else if(moves.get(moves.size()-1).correct)	//only if all moves so far were correct
    			{
    				//check whether this new move is correct based on the next move from the solution
    				if(moves.size() < solution.size() && isSame(solution.get(moves.size()), new Move(fromNumber, toNumber, true)))
    				{
    					moves.add(new Move(fromNumber, toNumber, true));
    				}
    				else
    				{
    					moves.add(new Move(fromNumber, toNumber, false));
    				}
    			}
    			
    			update(from, fromNumber, to, toNumber);
    		}
    		else System.out.println("Did nothing cause Disc to large");
    	}
    	else System.out.println("Did nothing cause Stack was empty");
    }
    
    //Are the two moves the same
    private boolean isSame(Move m1, Move m2)
    {
    	return (m1.from == m2.from && m2.to == m2.to);
    }
    //Are the two moves opposites from each other
    private boolean isOpposite(Move m1, Move m2)
    {
    	return (m1.to == m2.from && m1.from == m2.to);
    }
    
    private void update(ArrayList<Integer> from, int fromNumber, ArrayList<Integer> to, int toNumber)
    {
    	//Update Graphics by moving the Disc to the new Stack (to) and move Integer value from from to to
		//Graphics:
		Rectangle r = rectangles.get(from.get(from.size()-1));
		r.xProperty().bind(bars.get(toNumber).xProperty().subtract(r.widthProperty().divide(2).subtract(0.5*rbarA.getWidth())));
		r.yProperty().bind(rbase.yProperty().subtract((to.size()+1)*r.getHeight()));
		
		//Update Stack-Data:
		to.add(from.remove(from.size()-1));		//Disc that was on top of from is now on top of to
    }
    
    //Prepare the solution for comparison with user-made moves
    private void solve(int n, int from, int to, int aux, ArrayList<Move> m)
    {
    	if(n == 1)
    	{
    		m.add(new Move(from, to, true));
    	}
    	else
    	{
    		solve(n-1, from, aux, to, m);
    		m.add(new Move(from, to, true));
    		solve(n-1, aux, to, from, m);
    	}
    	
    }
    
    //class to save moves made by player and moves which are part of the solution
    private class Move
    {
    	Move(int from, int to, boolean correct)
    	{
    		this.from = from; 
    		this.to = to;
    		this.correct = correct;
    	}
    	int from;
    	int to;
    	boolean correct;
    }
    
    ArrayList<Move> moves;
    ArrayList<Move> solution;
    int target;
    int solCount;		//only when using PauseTransition

    // random color generating method
    private Color rndmColor(){
        int i = (int) (Math.random()*10+1);
        Color c=null;
        switch(i){
            case 1: c=Color.BURLYWOOD;break;
            case 2: c=Color.BLUE;break;
            case 3: c=Color.BROWN;break;
            case 4: c=Color.AZURE;break;
            case 5: c=Color.DARKGREEN; break;
            case 6: c=Color.DARKGREY; break;
            case 7: c=Color.BLANCHEDALMOND; break;
            case 8: c=Color.DARKMAGENTA;break;
            case 9: c=Color.DARKGOLDENROD;break;
            case 10: c=Color.CORNSILK;break;
            default : c=Color.RED;break;
        }
        return c;
    }
}
