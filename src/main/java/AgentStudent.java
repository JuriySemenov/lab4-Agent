/**
 * Created by User on 17.11.2016.
 */
import jade.core.*;

public class AgentStudent extends Agent {

    public void setup() {

        System.out.println("Я студент!!!");
        addBehaviour(new BehaviorStudent());
    }
      protected void takeDown(){
          System.out.println("Я сдал!!!");
    }
}
