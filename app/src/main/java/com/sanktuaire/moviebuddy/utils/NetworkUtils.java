package com.sanktuaire.moviebuddy.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.sanktuaire.moviebuddy.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Sanktuaire on 2017-04-07.
 */

public class NetworkUtils {
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org";
    private static final String API_VERSION = "3";
    private static final String TAG = NetworkUtils.class.getSimpleName();



    public static URL buildUrlMostPopular() {
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(API_VERSION)
                .appendPath("movie")
                .appendPath("popular")
                .appendQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(NetworkUtils.class.getSimpleName(), "Built URI " + url);

        return url;
    }

    public static URL buildUrlTopRated() {
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(API_VERSION)
                .appendPath("movie")
                .appendPath("top_rated")
                .appendQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(NetworkUtils.class.getSimpleName(), "Built URI " + url);

        return url;
    }

    public static String doTmdbQuery(String requestType) {
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        if (requestType.equals("popular"))
            builder.url(buildUrlMostPopular());
        else
            builder.url(buildUrlTopRated());
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            //Log.e(TAG, response.body().string());
            return response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e(TAG, "NOPE! NO RESPONSE!");
        return null;
    }




//    public String offline = "{\"page\":1,\"results\":[{\"poster_path\":\"\\/s9ye87pvq2IaDvjv9x4IOXVjvA7.jpg\",\"adult\":false,\"overview\":\"A koala named Buster recruits his best friend to help him drum up business for his theater by hosting a singing competition.\",\"release_date\":\"2016-11-23\",\"genre_ids\":[16,35,18,10751,10402],\"id\":335797,\"original_title\":\"Sing\",\"original_language\":\"en\",\"title\":\"Sing\",\"backdrop_path\":\"\\/fxDXp8un4qNY9b1dLd7SH6CKzC.jpg\",\"popularity\":76.289695,\"vote_count\":1025,\"video\":false,\"vote_average\":6.7},{\"poster_path\":\"\\/67NXPYvK92oQgEQvLppF2Siol9q.jpg\",\"adult\":false,\"overview\":\"A story about how a new baby's arrival impacts a family, told from the point of view of a delightfully unreliable narrator, a wildly imaginative 7 year old named Tim.\",\"release_date\":\"2017-03-23\",\"genre_ids\":[16,35,10751],\"id\":295693,\"original_title\":\"The Boss Baby\",\"original_language\":\"en\",\"title\":\"The Boss Baby\",\"backdrop_path\":\"\\/8keMlLuzB9XIUBnbdEq5DCqZdHQ.jpg\",\"popularity\":59.085865,\"vote_count\":219,\"video\":false,\"vote_average\":5.8},{\"poster_path\":\"\\/myRzRzCxdfUWjkJWgpHHZ1oGkJd.jpg\",\"adult\":false,\"overview\":\"In the near future, Major is the first of her kind: a human saved from a terrible crash, who is cyber-enhanced to be a perfect soldier devoted to stopping the world's most dangerous criminals.\",\"release_date\":\"2017-03-29\",\"genre_ids\":[28,18,878],\"id\":315837,\"original_title\":\"Ghost in the Shell\",\"original_language\":\"en\",\"title\":\"Ghost in the Shell\",\"backdrop_path\":\"\\/lsRhmB7m36pEX0UHpkpJSE48BW5.jpg\",\"popularity\":58.528733,\"vote_count\":298,\"video\":false,\"vote_average\":6.3},{\"poster_path\":\"\\/jjBgi2r5cRt36xF6iNUEhzscEcb.jpg\",\"adult\":false,\"overview\":\"Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.\",\"release_date\":\"2015-06-09\",\"genre_ids\":[28,12,878,53],\"id\":135397,\"original_title\":\"Jurassic World\",\"original_language\":\"en\",\"title\":\"Jurassic World\",\"backdrop_path\":\"\\/dkMD5qlogeRMiEixC4YNPUvax2T.jpg\",\"popularity\":44.043584,\"vote_count\":6725,\"video\":false,\"vote_average\":6.5},{\"poster_path\":\"\\/rXMWOZiCt6eMX22jWuTOSdQ98bY.jpg\",\"adult\":false,\"overview\":\"Though Kevin has evidenced 23 personalities to his trusted psychiatrist, Dr. Fletcher, there remains one still submerged who is set to materialize and dominate all the others. Compelled to abduct three teenage girls led by the willful, observant Casey, Kevin reaches a war for survival among all of those contained within him — as well as everyone around him — as the walls between his compartments shatter apart.\",\"release_date\":\"2017-01-19\",\"genre_ids\":[27,53],\"id\":381288,\"original_title\":\"Split\",\"original_language\":\"en\",\"title\":\"Split\",\"backdrop_path\":\"\\/4G6FNNLSIVrwSRZyFs91hQ3lZtD.jpg\",\"popularity\":41.379611,\"vote_count\":1592,\"video\":false,\"vote_average\":6.7},{\"poster_path\":\"\\/y31QB9kn3XSudA15tV7UWQ9XLuW.jpg\",\"adult\":false,\"overview\":\"Light years from Earth, 26 years after being abducted, Peter Quill finds himself the prime target of a manhunt after discovering an orb wanted by Ronan the Accuser.\",\"release_date\":\"2014-07-30\",\"genre_ids\":[28,878,12],\"id\":118340,\"original_title\":\"Guardians of the Galaxy\",\"original_language\":\"en\",\"title\":\"Guardians of the Galaxy\",\"backdrop_path\":\"\\/bHarw8xrmQeqf3t8HpuMY7zoK4x.jpg\",\"popularity\":27.482965,\"vote_count\":6734,\"video\":false,\"vote_average\":7.9},{\"poster_path\":\"\\/z09QAf8WbZncbitewNk6lKYMZsh.jpg\",\"adult\":false,\"overview\":\"Dory is reunited with her friends Nemo and Marlin in the search for answers about her past. What can she remember? Who are her parents? And where did she learn to speak Whale?\",\"release_date\":\"2016-06-16\",\"genre_ids\":[12,16,35,10751],\"id\":127380,\"original_title\":\"Finding Dory\",\"original_language\":\"en\",\"title\":\"Finding Dory\",\"backdrop_path\":\"\\/iWRKYHTFlsrxQtfQqFOQyceL83P.jpg\",\"popularity\":25.890039,\"vote_count\":2917,\"video\":false,\"vote_average\":6.7},{\"poster_path\":\"\\/inVq3FRqcYIRl2la8iZikYYxFNR.jpg\",\"adult\":false,\"overview\":\"Based upon Marvel Comics’ most unconventional anti-hero, DEADPOOL tells the origin story of former Special Forces operative turned mercenary Wade Wilson, who after being subjected to a rogue experiment that leaves him with accelerated healing powers, adopts the alter ego Deadpool. Armed with his new abilities and a dark, twisted sense of humor, Deadpool hunts down the man who nearly destroyed his life.\",\"release_date\":\"2016-02-09\",\"genre_ids\":[28,12,35,10749],\"id\":293660,\"original_title\":\"Deadpool\",\"original_language\":\"en\",\"title\":\"Deadpool\",\"backdrop_path\":\"\\/n1y094tVDFATSzkTnFxoGZ1qNsG.jpg\",\"popularity\":24.755287,\"vote_count\":7639,\"video\":false,\"vote_average\":7.3},{\"poster_path\":\"\\/kqjL17yufvn9OVLyXYpvtyrFfak.jpg\",\"adult\":false,\"overview\":\"An apocalyptic story set in the furthest reaches of our planet, in a stark desert landscape where humanity is broken, and most everyone is crazed fighting for the necessities of life. Within this world exist two rebels on the run who just might be able to restore order. There's Max, a man of action and a man of few words, who seeks peace of mind following the loss of his wife and child in the aftermath of the chaos. And Furiosa, a woman of action and a woman who believes her path to survival may be achieved if she can make it across the desert back to her childhood homeland.\",\"release_date\":\"2015-05-13\",\"genre_ids\":[28,12,878,53],\"id\":76341,\"original_title\":\"Mad Max: Fury Road\",\"original_language\":\"en\",\"title\":\"Mad Max: Fury Road\",\"backdrop_path\":\"\\/phszHPFVhPHhMZgo0fWTKBDQsJA.jpg\",\"popularity\":24.110022,\"vote_count\":7372,\"video\":false,\"vote_average\":7.2},{\"poster_path\":\"\\/h2mhfbEBGABSHo2vXG1ECMKAJa7.jpg\",\"adult\":false,\"overview\":\"The six-member crew of the International Space Station is tasked with studying a sample from Mars that may be the first proof of extra-terrestrial life, which proves more intelligent than ever expected.\",\"release_date\":\"2017-03-23\",\"genre_ids\":[27,878,53],\"id\":395992,\"original_title\":\"Life\",\"original_language\":\"en\",\"title\":\"Life\",\"backdrop_path\":\"\\/hES8wGmkxHa54z7hqUMpw5TIs09.jpg\",\"popularity\":22.607176,\"vote_count\":240,\"video\":false,\"vote_average\":6.1},{\"poster_path\":\"\\/5vHssUeVe25bMrof1HyaPyWgaP.jpg\",\"adult\":false,\"overview\":\"Ex-hitman John Wick comes out of retirement to track down the gangsters that took everything from him.\",\"release_date\":\"2014-10-22\",\"genre_ids\":[28,53],\"id\":245891,\"original_title\":\"John Wick\",\"original_language\":\"en\",\"title\":\"John Wick\",\"backdrop_path\":\"\\/mFb0ygcue4ITixDkdr7wm1Tdarx.jpg\",\"popularity\":21.616883,\"vote_count\":3665,\"video\":false,\"vote_average\":7},{\"poster_path\":\"\\/nHXiMnWUAUba2LZ0dFkNDVdvJ1o.jpg\",\"adult\":false,\"overview\":\"Vampire death dealer Selene fends off brutal attacks from both the Lycan clan and the Vampire faction that betrayed her. With her only allies, David and his father Thomas, she must stop the eternal war between Lycans and Vampires, even if it means she has to make the ultimate sacrifice.\",\"release_date\":\"2016-11-28\",\"genre_ids\":[28,27],\"id\":346672,\"original_title\":\"Underworld: Blood Wars\",\"original_language\":\"en\",\"title\":\"Underworld: Blood Wars\",\"backdrop_path\":\"\\/PIXSMakrO3s2dqA7mCvAAoVR0E.jpg\",\"popularity\":18.126697,\"vote_count\":1134,\"video\":false,\"vote_average\":4.9},{\"poster_path\":\"\\/sGFOggXN12CcSXD01hSAIaEoSgs.jpg\",\"adult\":false,\"overview\":\"An ancient urn is found in a cemetery outside Rome. Once opened, it triggers a series of violent incidents: robberies, rapes and murders increase dramatically, while several mysterious, evil-looking young women coming from all over the world are gathering in the city. All these events are caused by the return of Mater Lacrimarum, the last of three powerful witches who have been spreading terror and death for centuries. Alone against an army of psychos and demons, Sarah Mandy, an art student who seems to have supernatural abilities of her own, is the only person left to prevent the Mother of Tears from destroying Rome.\",\"release_date\":\"2007-09-06\",\"genre_ids\":[27],\"id\":15206,\"original_title\":\"La terza madre\",\"original_language\":\"it\",\"title\":\"The Mother of Tears\",\"backdrop_path\":\"\\/4LYJjYb506scNhugxcfCuxoSBAW.jpg\",\"popularity\":16.838949,\"vote_count\":83,\"video\":false,\"vote_average\":4.2},{\"poster_path\":\"\\/eezFoKz7bXgdbjeieeCYJFXPKSu.jpg\",\"adult\":false,\"overview\":\"Ned, an overprotective dad, visits his daughter at Stanford where he meets his biggest nightmare: her well-meaning but socially awkward Silicon Valley billionaire boyfriend, Laird. A rivalry develops and Ned's panic level goes through the roof when he finds himself lost in this glamorous high-tech world and learns Laird is about to pop the question.\",\"release_date\":\"2016-12-22\",\"genre_ids\":[35],\"id\":356305,\"original_title\":\"Why Him?\",\"original_language\":\"en\",\"title\":\"Why Him?\",\"backdrop_path\":\"\\/yYL6JJuSxCBtrp9EXGda37A2D6m.jpg\",\"popularity\":14.392495,\"vote_count\":460,\"video\":false,\"vote_average\":6.4},{\"poster_path\":\"\\/iRAZIEgfB9N0BObV0QI61Nxh92h.jpg\",\"adult\":false,\"overview\":\"Saban's Power Rangers follows five ordinary teens who must become something extraordinary when they learn that their small town of Angel Grove — and the world — is on the verge of being obliterated by an alien threat. Chosen by destiny, our heroes quickly discover they are the only ones who can save the planet. But to do so, they will have to overcome their real-life issues and before it’s too late, band together as the Power Rangers.\",\"release_date\":\"2017-03-23\",\"genre_ids\":[28,12,878],\"id\":305470,\"original_title\":\"Power Rangers\",\"original_language\":\"en\",\"title\":\"Power Rangers\",\"backdrop_path\":\"\\/eQkaPwMpJARFoPvbbNz2Z0Kye4O.jpg\",\"popularity\":13.903524,\"vote_count\":188,\"video\":false,\"vote_average\":6.6},{\"poster_path\":\"\\/6cbIDZLfwUTmttXTmNi8Mp3Rnmg.jpg\",\"adult\":false,\"overview\":\"The untold story of Katherine G. Johnson, Dorothy Vaughan and Mary Jackson – brilliant African-American women working at NASA and serving as the brains behind one of the greatest operations in history – the launch of astronaut John Glenn into orbit. The visionary trio crossed all gender and race lines to inspire generations to dream big.\",\"release_date\":\"2016-12-10\",\"genre_ids\":[36,18],\"id\":381284,\"original_title\":\"Hidden Figures\",\"original_language\":\"en\",\"title\":\"Hidden Figures\",\"backdrop_path\":\"\\/vifqDyPOB6jd5vwP2SIqWNtXUHu.jpg\",\"popularity\":13.08545,\"vote_count\":971,\"video\":false,\"vote_average\":7.7},{\"poster_path\":\"\\/sM33SANp9z6rXW8Itn7NnG1GOEs.jpg\",\"adult\":false,\"overview\":\"Determined to prove herself, Officer Judy Hopps, the first bunny on Zootopia's police force, jumps at the chance to crack her first case - even if it means partnering with scam-artist fox Nick Wilde to solve the mystery.\",\"release_date\":\"2016-02-11\",\"genre_ids\":[16,12,10751,35],\"id\":269149,\"original_title\":\"Zootopia\",\"original_language\":\"en\",\"title\":\"Zootopia\",\"backdrop_path\":\"\\/mhdeE1yShHTaDbJVdWyTlzFvNkr.jpg\",\"popularity\":12.696004,\"vote_count\":3253,\"video\":false,\"vote_average\":7.6},{\"poster_path\":\"\\/iBGRbLvg6kVc7wbS8wDdVHq6otm.jpg\",\"adult\":false,\"overview\":\"A five-year-old Indian boy gets lost on the streets of Calcutta, thousands of kilometers from home. He survives many challenges before being adopted by a couple in Australia; 25 years later, he sets out to find his lost family.\",\"release_date\":\"2016-11-24\",\"genre_ids\":[18],\"id\":334543,\"original_title\":\"Lion\",\"original_language\":\"en\",\"title\":\"Lion\",\"backdrop_path\":\"\\/zaePnCmDgul7jtyn3622gsujEZz.jpg\",\"popularity\":12.575466,\"vote_count\":689,\"video\":false,\"vote_average\":7.9},{\"poster_path\":\"\\/lIv1QinFqz4dlp5U4lQ6HaiskOZ.jpg\",\"adult\":false,\"overview\":\"Under the direction of a ruthless instructor, a talented young drummer begins to pursue perfection at any cost, even his humanity.\",\"release_date\":\"2014-10-10\",\"genre_ids\":[18,10402],\"id\":244786,\"original_title\":\"Whiplash\",\"original_language\":\"en\",\"title\":\"Whiplash\",\"backdrop_path\":\"\\/6bbZ6XyvgfjhQwbplnUh1LSj1ky.jpg\",\"popularity\":11.555049,\"vote_count\":3069,\"video\":false,\"vote_average\":8.3},{\"poster_path\":\"\\/5gJkVIVU7FDp7AfRAbPSvvdbre2.jpg\",\"adult\":false,\"overview\":\"A spacecraft traveling to a distant colony planet and transporting thousands of people has a malfunction in its sleep chambers. As a result, two passengers are awakened 90 years early.\",\"release_date\":\"2016-12-21\",\"genre_ids\":[12,18,10749,878],\"id\":274870,\"original_title\":\"Passengers\",\"original_language\":\"en\",\"title\":\"Passengers\",\"backdrop_path\":\"\\/5EW4TR3fWEqpKsWysNcBMtz9Sgp.jpg\",\"popularity\":11.279993,\"vote_count\":2136,\"video\":false,\"vote_average\":6.5}],\"total_results\":19438,\"total_pages\":972}";
}
