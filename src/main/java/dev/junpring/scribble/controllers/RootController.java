package dev.junpring.scribble.controllers;

import dev.junpring.scribble.dtos.ArticleListDto;
import dev.junpring.scribble.entities.board.ArticleEntity;
import dev.junpring.scribble.entities.board.BoardEntity;
import dev.junpring.scribble.services.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller(value = "dev.junpring.scribble.controllers.RootController")
@RequestMapping(value = "/")
public class RootController {
    private final BoardService boardServices;

    @Autowired
    public RootController(BoardService boardServices) {
        this.boardServices = boardServices;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getIndex(
            Model model
    ){
        List<BoardEntity> boardList = this.boardServices.getBoardList();

        HashMap<String, ArrayList<ArticleEntity>> articles = new HashMap<>();
        for (BoardEntity list : boardList) {
            if (!articles.containsKey(list.getCode())) {
                articles.put(list.getCode(), new ArrayList<>());
            }
            articles.get(list.getCode()).addAll(this.boardServices.rootArticleList(list));
        }
        model.addAttribute("articles", articles);
        model.addAttribute("boardList", boardList);

        List<ArticleListDto> getForPrintRcmdArticlesRs = this.boardServices.getForPrintRcmdArticles();
        model.addAttribute("getForPrintRcmdArticlesRs", getForPrintRcmdArticlesRs);
        return "root/index";

    }
}
