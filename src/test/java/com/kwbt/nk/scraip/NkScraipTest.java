package com.kwbt.nk.scraip;

import org.junit.Test;

public class NkScraipTest {

    private final NkScraip target = new NkScraip();

    @Test
    public void test() throws Exception {

        // 入力値
        NkInput input = new NkInput();
        input.set選択曜日(NkInput.曜日.土曜);
        input.set選択開催地(NkInput.開催地.東京);
        input.setラウンド(7);

        // テスト準備

        // テスト実行
        NkOutput output = target.letsScraip(input);

        // 評価

        //        System.out.println(output.getRaceTitle());
        //        System.out.println(output.getDistance());
        //        System.out.println(output.getSurface());

        //        output.getHorses().forEach(System.out::println);
    }

}
