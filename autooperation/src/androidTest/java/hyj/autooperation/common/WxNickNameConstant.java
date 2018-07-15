package hyj.autooperation.common;

import java.util.Random;

/**
 * Created by Administrator on 2017/11/5.
 */

public class WxNickNameConstant {

    private static String names="";
    private static String[] str;
    private static char c[] = {'a',
            'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
    static {
        names="  We always knew our daughter Kendall was going be a performer of some sort. She entertained people in our small town by putting on shows on our " +
                "front porch when she was only three or four. Blonde-haired, blue-eyed, and beautiful, she sang like a little angel and mesmerized1 everyone.When " +
                "Kendall was five, we began to notice that she was blinking a lot and clearing her throat frequently. We had her tested for allergies2, but the " +
                "doctor said she wasn't allergic3 to anything at all. After the problem worsened, we took her to our local children's hospital where she was diagnosed " +
                "with Tourette's Syndrome4 It was pretty devastating5 because other children constantly made fun of her, and sadly, even a teacher teased her. When " +
                "the tics were especially bad, Kendall had to wear a neck brace6. She only had one or two friends, but that was okay because they were -- and continue " +
                "to be real, the kind who stick by her, no matter what. Through all this, Kendall continued to sing and entertain. Remarkably7, her tics disappeared " +
                "when she sang We took our daughter from doctor to doctor, but all they did was give her medication that just made it worse, so we decided8 to go the " +
                "natural route. Through chiropractic therapy, changes in her diet, and other natural treatments, the tics gradually lessened9 At a birthday party, Kendall" +
                "hopped10 on a friend for a piggyback ride. He bent11 lower than she expected, and she jumped higher than he expected. Kendall flew over his back and landed " +
                "on the cement floor  on her neck. An ambulance rushed her to the hospital where she spent the next week, paralyzed from the neck down. Ironically, her " +
                "biggest concern wasn't whether she would walk again, but whether she would be able to audition I believe Kendall wanted the American Idol audition so much " +
                "that she willed herself to move again. One of her friends brought a microphone to the hospital and put it on her bed. Every day, Kendall tried hard to pick it " +
                "up with her right hand. It was more important for her to pick up that mic than a spoon or forkKendall is eighteen now, living every day to its fullest. " +
                "She's recorded a CD with some of John Mellencamp's band members. She's also on CMT's Music City Madness for an original song and video, and is having some " +
                "good success. I'm absolutely sure she's going to make it big some day. Kendall just puts it all in God's hands.";
        str = names.split(" ");
    }
    private  static String getName(){
        Random rand = new Random();
        int randNum = rand.nextInt(str.length-1);
        String rs = str[randNum];
        return rs.replaceAll("'|\\.|,|\\s", "");
    }
    public static String getName1() {
        String s = getName();
        while(s==null||s.length()<3) {
            s = getName();
        }
        String head = (String.valueOf(c[new Random().nextInt(25)])+String.valueOf(c[new Random().nextInt(25)])+String.valueOf(c[new Random().nextInt(25)])).toUpperCase();
        s = head+s;
        return s;

    }
    public static void main(String[] args) {
        for(int i=0,l=str.length;i<l;i++) {
            System.out.println(getName1());
        }
        System.out.println("s'dfd.sdf".replaceAll("'|\\.|,|\\s", ""));
    }

}
