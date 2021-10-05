package proger.savcheg.cibinternstesttask;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<List<Sock>> getSocksParam(@RequestParam("color") String color,
                                             @RequestParam("operation") String operation,
                                             @RequestParam("cottonPart") int cottonPart) {
        return new ResponseEntity<>(sockDAO.showWithParam(color, operation, cottonPart), HttpStatus.OK);
    }

    /**
     * POST запросы которые принимают JSON
     */
    @PostMapping("/socks/income")
    ResponseEntity incomeSocks(@RequestBody Sock sock) {
        sock.setId(sock.hashCode());
        return new ResponseEntity(null, sockDAO.income(sock));
    }

    @PostMapping("/socks/outcome")
    ResponseEntity outcomeSocks(@RequestBody Sock sock) {
        sock.setId(sock.hashCode());
        return new ResponseEntity(null, sockDAO.outcome(sock));
    }
}
