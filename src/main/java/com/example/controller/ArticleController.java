package com.example.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.model.Article;
import com.example.service.ArticleService;

@Controller
@RequestMapping("/article")
public class ArticleController {

  @Autowired
  ArticleService service;

  // index
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ModelAndView index(ModelAndView mav) {
    List<Article> datalist = service.findAll();
    mav.setViewName("article/index");
    mav.addObject("datalist", datalist);
    return mav;
  }

  // create
  @RequestMapping(value = "/create", method = RequestMethod.GET)
  public ModelAndView create(ModelAndView mav) {
    mav.setViewName("article/create");
    return mav;
  }

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  public ModelAndView post_create(@ModelAttribute("formModel") Article article, ModelAndView mav) {
    // 日付の設定
    article.setCreated_at(new Date());
    article.setUpdated_at(new Date());
    service.create(article);
    return new ModelAndView("redirect:/article/");
  }

  // read
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ModelAndView show(@PathVariable("id") Integer id, ModelAndView mav) {
    mav.setViewName("/article/show");
    Article data = service.findOne(id);
    mav.addObject("data", data);
    return mav;
  }

  // update
  @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
  public ModelAndView edit(@PathVariable("id") Integer id, ModelAndView mav) {
    mav.setViewName("/article/edit");
    Article data = service.findOne(id);
    mav.addObject("formModel", data);
    return mav;
  }

  @RequestMapping(value = "/{id}/edit", method = RequestMethod.PUT)
  public ModelAndView post_edit(@ModelAttribute("formModel") Article article ,BindingResult result, ModelAndView mav) {
    // 更新日付の更新
    article.setCreated_at(service.findOne(article.getId()).getCreated_at());
    article.setUpdated_at(new Date());
    service.update(article);
    return new ModelAndView("redirect:/article/" + article.getId());
  }

  // delete
  @RequestMapping(value = "/{id}/delete" , method = RequestMethod.GET)
  public ModelAndView delete(@PathVariable Integer id,RedirectAttributes attributes,ModelAndView mav){
    service.delete(id);
    attributes.addFlashAttribute("deleteMessage", "delete ID:" + id);
    return new ModelAndView("redirect:/article/");
  }

}