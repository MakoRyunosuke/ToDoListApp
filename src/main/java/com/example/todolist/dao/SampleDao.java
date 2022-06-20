package com.example.todolist.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.todolist.entity.EntForm;

@Repository
public class SampleDao {
    
    private final JdbcTemplate db;

    @Autowired
    public SampleDao(JdbcTemplate db) {
        this.db = db;
    }

    public void insertDb(EntForm entForm){
        this.db.update("INSERT INTO task (title, detail) VALUES (?, ?)", entForm.getTitle(), entForm.getDetail());
    }

    public List<EntForm> searchDb(){
        List<EntForm> list = new ArrayList<EntForm>();
        List<Map<String, Object>> rows = this.db.queryForList("SELECT * FROM task");
        for (Map<String, Object> row : rows) {
            EntForm form = new EntForm();
            form.setId((int)row.get("id"));
            form.setTitle((String)row.get("title"));
            form.setDetail((String)row.get("detail"));
            form.setDone((Boolean)row.get("done"));
            list.add(form);
        }
        return list;
    }

    public void deleteDb(int id)
	{
		db.update("DELETE FROM task WHERE id = ?", id);
		
		System.out.println("削除しました");
	}

    public void updateDb(int id, EntForm entForm)
    {
        db.update("UPDATE task SET title = ?, detail = ? WHERE id = ?", entForm.getTitle(), entForm.getDetail(), id);

        System.out.println("更新しました");
    }

    public List<EntForm> selectOne (int id){
        //コンソールに表示
        System.out.println("編集画面を出します");

        //データベースから目的の 1 件を取り出して、そのまま resultDB1 に入れる
        List<Map<String, Object>> resultDb1 = db.queryForList ("SELECT * FROM task where id=?",id);

        //画面に表示しやすい形の List(resultDB2) を用意
        List<EntForm> resultDb2=new ArrayList <EntForm>();

        //1件ずつピックアップ
        for(Map<String,Object> result1:resultDb1){

            //データ 1 件分を 1 つのまとまりとするので、 EntForm 型の「 entformdb 」を生成
            EntForm entformdb = new EntForm();

            //id、 name のデータを entformdb に移す
            entformdb.setId((int)result1.get("id"));
            entformdb.setTitle((String)result1.get("title"));
            entformdb.setDetail((String)result1.get("detail"));
            entformdb.setDone((Boolean)result1.get("done"));
            
            //移し替えたデータを持った entformdb を、 resultDB2 に入れる
            resultDb2.add(entformdb);
        }
        //Controllerに渡す
        return resultDb2;
    }
}
