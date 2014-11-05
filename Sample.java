import java.io.IOException;
import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;
import java.lang.Math;
import java.io.*;
import java.util.Random;
import java.util.ArrayList;

//Make listener

class LeapListener extends Listener {
	public void onInit(Controller controller){
		System.out.println("Initialized");
	}
	
	public void onConnect(Controller controller){ //Initial welcome message to user
		System.out.println("Connected to Motion Sensor");
		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
		System.out.println("Let's play Rock, Paper or Scissors!");
		int i = 0;
		for(i=3; i>0; i-=1){
			System.out.println(i);
			if(i==1){
				System.out.println("GO! Choose Rock, Paper or Scissors!");
			}
			
		}
	}
	
	public void onDisconnect(Controller controller){
		System.out.println("Motion Sensor Disconnected");
	}
	public void onExit(Controller controller){
		System.out.println("Exited");
	}
	
	public void onFrame(Controller controller){
		Frame frame = controller.frame(); //The latest frame
		int computer_count = 0;
		int player_count = 0;
		for (Finger finger: frame.fingers()){ //Need both hand and fingers as we are working with some fingers and the entire palm
			for(Hand hand : frame.hands()){
				float pinch = hand.pinchStrength(); 
				float strength = hand.grabStrength();
				FingerList extendedFingerList = hand.fingers().extended(); //Get only the entended fingers and store them in a list
				FingerList index_FingerList = hand.fingers().fingerType(Finger.Type.TYPE_INDEX); //Get only a list of "index" fingers. One one will be stored, unless using both hands. 
				Finger index_Finger = index_FingerList.get(0); 
			
				FingerList pinkyFingerList = hand.fingers().fingerType(Finger.Type.TYPE_MIDDLE);
				Finger middle_Finger = pinkyFingerList.get(0);
				
				
				ArrayList<String> list = new ArrayList<String>(); //Storing an ArrayList of items that the computer will call upon at random
				list.add("Rock");
				list.add("Paper");
				list.add("Scissors");
				Random rand = new Random();
				int index = rand.nextInt(list.size());
				
				if(extendedFingerList.get(0).type() == Finger.Type.TYPE_INDEX && extendedFingerList.get(1).type() == Finger.Type.TYPE_MIDDLE && pinch != 0 && strength != 1){ //Check if first extended finger is index and second middle
																																											  // Also ignore pinch and strength to avoid confusion with scissors and rock				
					
					System.out.print("You chose scissors!\n");
					//if(computer chose paper, computer loses - player has score count of 1)
					//if(computer chose rock, player loses - computer has score count of 1)
					//if(computer chose scissors, draw - score counts are not updated)
					
					if(list.get(index)== "Scissors"){
						System.out.println("Draw. No one gets a point.");
						System.out.println("Player score: "+player_count+ " Computer score: "+computer_count);
						System.out.println();
					}
					if(list.get(index) == "Rock"){
						System.out.println("Player loses, computer gets a point.");
						computer_count++;
						System.out.println("Player score: "+player_count+ " Computer score: "+computer_count);
						System.out.println();
					}
					if(list.get(index) == "Paper"){
						System.out.println("Computer loses, player gets a point.");
						player_count++;
						System.out.println("Player score: "+player_count+ " Computer score: "+computer_count);
						System.out.println();
					}
					
				}
				
			
				
				if(pinch == 0){
					System.out.print("You chose paper!\n");
					if(list.get(index)== "Paper"){
							System.out.println("Draw. No one gets a point.");
							System.out.println("Player score: "+player_count+ " Computer score: "+computer_count);
							System.out.println();
					}
					if(list.get(index) == "Scissors"){
						System.out.println("Player loses, computer gets a point.");
						computer_count++;
						System.out.println("Player score: "+player_count+ " Computer score: "+computer_count);
						System.out.println();
					}
					if(list.get(index) == "Rock"){
						System.out.println("Computer loses, player gets a point.");
						player_count++;
						System.out.println("Player score: "+player_count+ " Computer score: "+computer_count);
						System.out.println();
					}
					
					
				}
				
				
				if(strength == 1){
					System.out.print("You chose rock!\n");
					if(list.get(index)== "Rock"){
						System.out.println("Draw. No one gets a point.");
						System.out.println("Player score: "+player_count+ " Computer score: "+computer_count);
						System.out.println();
					}
					if(list.get(index) == "Paper"){
						System.out.println("Player loses, computer gets a point.");
						computer_count++;
						System.out.println("Player score: "+player_count+ " Computer score: "+computer_count);
						System.out.println();
					}
					if(list.get(index) == "Scissors"){
						System.out.println("Computer loses, player gets a point.");
						player_count++;
						System.out.println("Player score: "+player_count+ " Computer score: "+computer_count);
						System.out.println();
					}
				}
					
				
				
	
			}
		}	
		
		GestureList gestures = frame.gestures();
		for (int i=0; i<gestures.count(); i++){
			Gesture gesture = gestures.get(i);
			
			switch (gesture.type()){
				case TYPE_CIRCLE:
					CircleGesture circle = new CircleGesture(gesture);
					
					String clockwiseness;
					if(circle.pointable().direction().angleTo(circle.normal()) <= Math.PI/4) {
						clockwiseness = "clockwise";
					}else{
						clockwiseness = "counter-clockwise";
					}
					
					double sweptAngle = 0;
					if (circle.state()!= State.STATE_START){
						CircleGesture previous = new CircleGesture(controller.frame(1).gesture(circle.id()));
						sweptAngle = (circle.progress() - previous.progress()) * 2 * Math.PI;
					}
					if(clockwiseness == "clockwise"){
						System.out.println("New Game has commenced. Get Ready!");
					}else{System.out.println("Game quit. Let's play again soon?");}
					
					break;

				default:
					System.out.println("Unknown gesture!!!");
			}
		}
		
	}	
}

public class Sample {
		
	public static void main(String[] args){
		LeapListener listener = new LeapListener();
		Controller controller = new Controller();
		
			
		controller.addListener(listener);
			
		System.out.println("Press Enter to quit");
			
		try{
			System.in.read();
		}catch (IOException e){
			e.printStackTrace();
		}
			
		controller.removeListener(listener);
	}
}
