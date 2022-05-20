package com.example.demo.service.impl;

import com.example.demo.entity.Article;
import com.example.demo.mapper.ArticleMapper;
import com.example.demo.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author fll
 * @since 2022-04-19
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

}
