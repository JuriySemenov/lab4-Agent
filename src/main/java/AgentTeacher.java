/**
 * Created by User on 17.11.2016.
 */

import jade.core.*;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;


public class AgentTeacher extends Agent {


    public void setup() {
        //создание DFAgentDescription
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        //Создания сервиса
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Exam");
        sd.setName("Examination");

       //добовления сервиса
        dfd.addServices(sd);

        try {
            //регестрация сервиса
            DFService.register(this, dfd);
        }
        catch (FIPAException fe){
            fe.printStackTrace();
        }
        //старт поведения
        System.out.println("Я учитель!!!");
        addBehaviour(new BehaviorTeacher());

    }

    /**
     * выполняется при завершинии работы агента
     */
    protected void takeDown(){
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        System.out.println("Пора домой.");
    }
}
