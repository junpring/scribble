package dev.junpring.scribble.controllers;

import dev.junpring.scribble.entities.board.BoardEntity;
import dev.junpring.scribble.services.BoardService;
import dev.junpring.scribble.vos.board.article.ArticleListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView getIdx(
            ModelAndView modelAndView,
            ArticleListVo articleVo
    ){
        List<BoardEntity> boardIdVos = this.boardServices.boardList();

        HashMap<String, ArrayList<ArticleListVo>> articles = new HashMap<>();
        for (BoardEntity idVo : boardIdVos) {
            if (!articles.containsKey(idVo.getCode())) {
                articles.put(idVo.getCode(), new ArrayList<>());
            }
//            articleVo.setArticleId(idVo.getId());
            articles.get(idVo.getCode()).addAll(this.boardServices.rootArticleList(idVo));
        }

        modelAndView.addObject("articles", articles);
        modelAndView.addObject("boardIdVos", boardIdVos);
        modelAndView.setViewName("root/index");
        return modelAndView;
    }
}
