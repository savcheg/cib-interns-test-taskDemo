package proger.savcheg.cibinternstesttask;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SockDAO {
    private List<Sock> sockList;

    {
        sockList = new ArrayList<>();

        sockList.add(new Sock("red", 50, 20));
        sockList.add(new Sock("blue", 30, 30));
        sockList.add(new Sock("blue", 30, 25));
    }

    public List<Sock> showAll() {
        return sockList;
    }

    public List<Sock> showWithParam(String color, String operation, int cottonPart) {
        if (operation.equals("moreThan"))
            return sockList.stream().filter(sock -> sock.getColor().equals(color)
                    && sock.getCottonPart() > cottonPart).collect(Collectors.toList());
        if (operation.equals("lessThan"))
            return sockList.stream().filter(sock -> sock.getColor().equals(color)
                    && sock.getCottonPart() < cottonPart).collect(Collectors.toList());
        if (operation.equals("equal"))
            return sockList.stream().filter(sock -> sock.getColor().equals(color)
                    && sock.getCottonPart() == cottonPart).collect(Collectors.toList());
        return null;
    }

    public void income(String color, int cottonPart, int quantity) { //принимает JSON файл
        /**
         * Если в БД есть объект с id = hash(color, cottonPart) то увеличить количество пар носков
         *
         * Иначе создать новый объект в таблице
         */
        //if()
        //sockList.add(new Sock(color, cottonPart, quantity));
    }

    public void outcome(Sock sock) {//принимает JSON файл
        /**
         * findById...
         */
    }
}
