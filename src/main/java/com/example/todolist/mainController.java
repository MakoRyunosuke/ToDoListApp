package com.example.todolist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.todolist.dao.SampleDao;
import com.example.todolist.entity.EntForm;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class mainController {

    private final SampleDao sampleDao;

    @Autowired
	public mainController(SampleDao sampleDao) {
		this.sampleDao = sampleDao;
	}
    
    @RequestMapping("/")
    public String index(Model model) {

	List<EntForm> list = sampleDao.searchDb();
    model.addAttribute("dbList", list);
		
	return "index";
    
    }

    @RequestMapping("/form")
	public String form(Model model, Form form)
	{
		return ("input");
	}

    @RequestMapping("/confirm")
	public String confirm(Model model, @Validated Form form, BindingResult result)
	{
		if (result.hasErrors()) {
			/* 入力内容にエラーがあった場合の動作：元の画面に戻る */
			return ("input");
		}
		return ("confirm");
	}

    @RequestMapping("/complete")
	public String complete(Form form, Model model)
	{
		/* EntFormをSampleDaoに渡したいので、newする */
		EntForm entForm = new EntForm();
		
		/* formオブジェクトに入っている、ユーザーが画面で入力した値を、entFormに渡す */
		entForm.setId( form.getId() );
        entForm.setTitle( form.getTitle() );
        entForm.setDetail( form.getDetail() );
        entForm.setDone( form.getDone() );
		
		/* SampleDaoにEntFormのオブジェクトを渡して、データベースに保存させる */
		this.sampleDao.insertDb(entForm);
		
		// return "complete";
        return "redirect:/";
	}
    
    @RequestMapping("del/{id}")
	public String destoroy(
		@PathVariable int id
	)
	{
		/* 指定のIDのデータを削除する */
		sampleDao.deleteDb(id);
		
		/* redirect:<URL> で、指定のURLに遷移する */
		return "redirect:/";
	}

    @RequestMapping("/edit/{id}")
    public String editView (@PathVariable int id, Model model){  

    //DBからデータを 1 件取ってくる リストの形
        List<EntForm> list = sampleDao.selectOne (id);

        //リストから、オブジェクトだけをピックアップ
        EntForm entformdb = list.get (0);

        //スタンバイしている View に向かって、データを投げる
        model.addAttribute("form", entformdb);
        model.addAttribute("title", "編集ページ");
        return "edit";
    }

    @RequestMapping("/edit/{id}/exe")
    public String editExe (@PathVariable int id, Model model , Form form){

        //フォームの値をエンティティに入れ直し
        EntForm entform = new EntForm();
        entform.setTitle(form.getTitle());
        entform.setDetail(form.getDetail());

        //更新の実行
        sampleDao.updateDb(id,entform);
    //一覧画面へリダイレクト
    return "redirect:/";
    }
}
