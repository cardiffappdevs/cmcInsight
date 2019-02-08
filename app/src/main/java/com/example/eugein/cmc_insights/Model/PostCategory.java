package com.example.eugein.cmc_insights.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Eugein on 4/3/18.
 */

public class PostCategory implements Serializable {
    private String category_id;
    private String category_name;
    private ArrayList<Post>post_list;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public ArrayList<Post> getPost_list() {
        return post_list;
    }

    public void setPost_list(ArrayList<Post> post_list) {
        this.post_list = post_list;
    }
}
