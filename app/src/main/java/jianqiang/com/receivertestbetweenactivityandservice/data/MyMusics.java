package jianqiang.com.receivertestbetweenactivityandservice.data;

public class MyMusics {
    public static Music[] musics = null;

    static {
        Music music1 = new Music("11.mp3", "包包 翻唱", "新不了情");
        Music music2 = new Music("12.mp3", "郭包肉组合 合唱", "淘汰");
        Music music3 = new Music("13.mp3", "郭郭 演绎别人的", "爱与痛的边缘");
        musics = new Music[]{ music1, music2, music3};
    }
}