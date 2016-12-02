import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.Date;
import java.util.Random;

/**
 * Created by User on 17.11.2016.
 * @author Semenov
 */
class BehaviorStudent extends Behaviour {

    AID teacher; //агент прнимающий экзамен
    double[] billets = new double[100]; //массив всех вопросов
    ACLMessage recv; //сообщение полученное от других агентов

    /**
     * Учит вопросы перед сдачей
     */
    void study() {

        Date date = new Date();
        Random random = new Random(date.getTime());

        for (int i = 0; i < 10; i++) {

            int n = random.nextInt() % 99;

            if (n < 0) n *= -1;

            double pr = (double) (random.nextInt() % 100) / 100;

            if (pr < 0) pr *= -1;

            billets[n] += pr;
        }
    }

    /**
     *На основе выученных билетов отвечает на вопрос.
     * @param billet билет с вопросами
     * @return количество набранных балов
     */
    private double answer(String billet) {
        String[] vopros;
        vopros = billet.split(" ");
        double an =0.0;
        for (int i = 1; i < vopros.length; i++){
            an = an + billets[Integer.parseInt(vopros[i])];
        }
        return an;
    }

    /**
     * Выполняется при старте поведения
     *
     * Поиск агента предоставляющего сервис "Exam" в каталоге
     * и его инициализация
     */
    @Override
    public void onStart() {
        //немного подождать что бы агент преподователя успел загрузится
        myAgent.doWait(100);

        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Exam");
        template.addServices(sd);
        teacher = new AID();

        try {
            //получения списка всех агентов с предоставляющих данный сервис
            DFAgentDescription[] result = DFService.search(myAgent, template);

            //инициализация первым из списка
            teacher = result[0].getName();
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void action() {

        study();

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);

        msg.addReceiver(teacher);
        msg.setContent("Можно сдать?");

        myAgent.send(msg);

        System.out.println("Студент: Можно сдать?");

        //ожидания вопросов
        while (recv==null) {
            recv = myAgent.receive();
            myAgent.doWait(100);
        }


        String result = String.valueOf(answer(recv.getContent()));

        msg.setContent(result);

        myAgent.send(msg);
        System.out.println("Студент набрал " + result + "балов.");

        //ожидание решения преподователя
        recv=null;
        while (recv==null) {
            recv = myAgent.receive();
            myAgent.doWait(100);
        }

       //завершение работы агента в случае успешной сдачи
        if(recv.getContent().contains("Давай зачетку")){
            myAgent.doDelete();
        }
        //повтор цикла
        else {
            recv=null;
            action();
        }
    }


    /**
     * Метод вызывается при завершинии поведения
     * @return всегда возвращает true
     */
    @Override
    public boolean done() {
        return true;
    }

}
