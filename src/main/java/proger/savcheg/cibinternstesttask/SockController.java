package proger.savcheg.cibinternstesttask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SockController {

    private final SockDAO sockDAO;

    public SockController(SockDAO sockDAO) {
        this.sockDAO = sockDAO;
    }

    @GetMapping()
    List<Sock> getSocks() {
        return sockDAO.showAll();
    }

    @GetMapping("/socks")
    @ResponseBody
    List<Sock> getSocksParam(@RequestParam("color") String color,
                             @RequestParam("operation") String operation,
                             @RequestParam("cottonPart") int cottonPart) {
        System.out.println(color + " " + operation + " " + cottonPart);
        List<Sock> list = sockDAO.showWithParam(color, operation, cottonPart);
        System.out.println(list);
        return list;
    }

    /**
     * POST запрос который передает JSON
     */
    @PostMapping("/socks/income")
    void incomeSocks(@RequestParam("color") String color,
                     @RequestParam("cottonPart") int cottonPart,
                     @RequestParam("quantity") int quantity) {
        sockDAO.income(color, cottonPart, quantity);
    }

    @PostMapping("/socks/outcome")
    void incomeSocks(@RequestBody Sock sock) {
        sockDAO.outcome(sock);
    }
}
