import java.io.IOException;

import mlt.moses.MosesEngine;
import mlt.moses.MosesException;


public class test_moses
{
	public static void main(String[] args) throws MosesException, IOException
	{
		System.out.println(new java.io.File( "." ).getCanonicalPath());
		
        MosesEngine mosesEngine = new MosesEngine();

        //System.out.println(mosesEngine.translate("mein neues Haus"));
        //System.out.println(mosesEngine.translate("haus klein"));
        
        System.out.println("## " + mosesEngine.translate("一 种 青 藏 铁"));//  路 多 年 冻 土 隧 道 支 护 方 法 ， 其 特 征 在 于 包 括 以 下 操 作 歩 骤
	}
}
