package movie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/movie")
public class MovieController {

    @GetMapping("/ticketingdate")
    public String ticketing(){
        return "movie/ticketingdate";
    }

    @GetMapping("/ticketingseat")
    public  String ticketingseat(){return "movie/ticketingseat";}

}
