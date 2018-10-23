package com.example.xgramajo.parkme_ids_2018;

import org.junit.Test;
import org.junit.Assert;

public class PatentActivityTest {

    @Test
    public void verifyPatent() throws Exception {
        //True cases
        Assert.assertTrue(PatentActivity.verifyPatent("ABC123"));
        Assert.assertTrue(PatentActivity.verifyPatent("AB123CD"));
        //False cases
        Assert.assertFalse(PatentActivity.verifyPatent("ABC 123"));
        Assert.assertFalse(PatentActivity.verifyPatent("AB 123 CD"));
        Assert.assertFalse(PatentActivity.verifyPatent(""));
        Assert.assertFalse(PatentActivity.verifyPatent("ABCDEF"));
        Assert.assertFalse(PatentActivity.verifyPatent("ABCDEFG"));
    }
}
