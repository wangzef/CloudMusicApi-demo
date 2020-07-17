package com.cloudmusic.controller.cloudMusic;

import com.cloudmusic.api.CloudMusicApiUrl;
import com.cloudmusic.request.cloudMusic.CreateWebRequest;
import com.cloudmusic.result.Result;
import com.cloudmusic.request.cloudMusic.ResultCacheUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 接口功能：有关的歌曲内容
 * Created by xuzijia
 * 2018/5/22 15:54
 * 为使用方便,降低门槛,所有接口直接使用了 Get 请求
 */
@RestController
public class MusicController {
    @Autowired
    private ResultCacheUtils resultCacheUtils;
    /**
     * 获取歌曲详细信息
     * @param id *歌曲id 必传
     * @return 歌曲详细内容
     */
    @RequestMapping("/song/detail")
    public String getSongDetail(String id) throws Exception {
        if(id==null || id.trim().equals("")){
            return new JSONObject(new Result(0, "缺少必填参数")).toString();
        }
        String url= CloudMusicApiUrl.SongDetailUrl.replace("{id}",id);
        return CreateWebRequest.createWebPostRequest(url,new HashMap<>(),new HashMap<>());
    }
    /**
     * 获取歌曲歌词
     * @param id *歌曲id 必传
     * @return 歌曲歌词
     */
    @RequestMapping("/song/lyric")
    public String getSongLyric(String id) throws Exception {
        if(id==null || id.trim().equals("")){
            return new JSONObject(new Result(0, "缺少必填参数")).toString();
        }
        String url= CloudMusicApiUrl.SongLyricUrl;
        Map<String,Object> params=new HashMap<>();
        params.put("id",id);
        params.put("lv","-1");
        params.put("kv","-1");
        params.put("tv","-1");
        Map<String,String> cookie = new HashMap<>();
        cookie.put("os","pc");
        return CreateWebRequest.createWebPostRequest(url,params,cookie);
    }

    /**
     * http://music.163.com/song/media/outer/url?id={id}.mp3
     * 获取歌曲播放地址(通过组装url方式返回)
     * @param ids *歌曲id(多个id用逗号分开) 必传
     * @return 歌曲url
     */
    @RequestMapping("/song/url")
    public String getSongUrl(String ids) throws Exception {
        if(ids==null || ids.trim().equals("")){
            return new JSONObject(new Result(0, "缺少必填参数")).toString();
        }
        Map<String,String> data=new HashMap<>();
        String[] split = ids.split(",");
        for(String id:split){
            data.put(id, CloudMusicApiUrl.FinalSongUrl.replace("{id}",id));
        }
        return new JSONObject(data).toString();
    }

    /**
     * 获取歌曲播放地址(POST请求) 如果code=404 说明歌曲没有版权或者歌曲不存在
     * @param ids *歌曲id(多个id用逗号分开) 必传
     * @param br 码率(默认值：最大码率)
     * @return 歌曲url
     */
    @RequestMapping("/song/player")
    public String getSongPlayer(String ids,Integer br) throws Exception {
        if(ids==null || ids.trim().equals("")){
            return new JSONObject(new Result(0, "缺少必填参数")).toString();
        }
        br=br==null?999000:br;
        Map<String,Object> data=new HashMap<>();
        data.put("ids",ids.split(","));
        data.put("br",br);
        return CreateWebRequest.createWebPostRequest(CloudMusicApiUrl.songPlayerUrl,data,new HashMap<>());
    }

    /**
     * 推荐的新音乐
     * @return 音乐列表
     */
    @RequestMapping("/song/personalized")
    public String getSongPersonalized() throws Exception {
        Map<String,Object> data=new HashMap<>();
        data.put("type","recommend");
        return CreateWebRequest.createWebPostRequest(CloudMusicApiUrl.songPersonalizedUrl,data,new HashMap<>());
    }

}
