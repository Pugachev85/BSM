package com.pugachev85.b_s_m.domain;

import static com.pugachev85.b_s_m.domain.StudyPlaceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.pugachev85.b_s_m.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudyPlaceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudyPlace.class);
        StudyPlace studyPlace1 = getStudyPlaceSample1();
        StudyPlace studyPlace2 = new StudyPlace();
        assertThat(studyPlace1).isNotEqualTo(studyPlace2);

        studyPlace2.setId(studyPlace1.getId());
        assertThat(studyPlace1).isEqualTo(studyPlace2);

        studyPlace2 = getStudyPlaceSample2();
        assertThat(studyPlace1).isNotEqualTo(studyPlace2);
    }
}
