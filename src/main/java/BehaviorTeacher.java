import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Date;
import java.util.Random;

/**
 * Created by User on 17.11.2016.
 *
 * @author semenov
 */
class BehaviorTeacher extends CyclicBehaviour {

    /**
     * Метод для выбора вопросов
     *
     * @return список вопросов
     */
    String chooseBillet() {

        String asks = "";
        Date date = new Date();
        Random random = new Random(date.getTime());

        for (int i = 0; i < 5; i++) {

            int num = random.nextInt() % 100;
            if (num < 0) num *= -1;
            asks = asks + " " + num;
        }

        return asks;
    }

    /**
     * Основное поведения агента выполняется циклично
     */
    @Override
    public void action() {

        myAgent.doWait(100);

        ACLMessage msg = myAgent.receive();

        if (msg != null) {

            String content = msg.getContent();
            ACLMessage mes = new ACLMessage(ACLMessage.REQUEST);
            mes.addReceiver(msg.getSender());

            //обработка принятого сообщения
            switch (content) {

                case ("Можно сдать?"): {
                    String str = chooseBillet();
                    mes.setContent("Вопросы:" + str);
                    System.out.println("Преподователь: Вот вопросы " + str);
                    break;

                }

                default: {

                    Double mark = Double.parseDouble(content);
                    if (mark < 2) {
                        mes.setContent("Не сдал. Иди готовся.");
                        System.out.println("Не сдал. Иди готовся.");
                    } else {
                        mes.setContent("Давай зачетку " + (mark.intValue() + 1));
                        System.out.println("Давай зачетку " + (mark.intValue() + 1));
                    }

                }
            }
            //ответ агенту на основе обработанной информации
            myAgent.send(mes);
        } else {
            block();//ожидает пока не придет сообщение
        }
    }

}
