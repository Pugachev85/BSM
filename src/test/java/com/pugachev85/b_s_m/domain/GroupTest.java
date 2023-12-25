package com.pugachev85.b_s_m.domain;

import static com.pugachev85.b_s_m.domain.EducationalProgramTestSamples.*;
import static com.pugachev85.b_s_m.domain.GroupTestSamples.*;
import static com.pugachev85.b_s_m.domain.StudyPlaceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.pugachev85.b_s_m.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Group.class);
        Group group1 = getGroupSample1();
        Group group2 = new Group();
        assertThat(group1).isNotEqualTo(group2);

        group2.setId(group1.getId());
        assertThat(group1).isEqualTo(group2);

        group2 = getGroupSample2();
        assertThat(group1).isNotEqualTo(group2);
    }

    @Test
    void studyPlaceTest() throws Exception {
        Group group = getGroupRandomSampleGenerator();
        StudyPlace studyPlaceBack = getStudyPlaceRandomSampleGenerator();

        group.setStudyPlace(studyPlaceBack);
        assertThat(group.getStudyPlace()).isEqualTo(studyPlaceBack);

        group.studyPlace(null);
        assertThat(group.getStudyPlace()).isNull();
    }

    @Test
    void educationalProgramTest() throws Exception {
        Group group = getGroupRandomSampleGenerator();
        EducationalProgram educationalProgramBack = getEducationalProgramRandomSampleGenerator();

        group.setEducationalProgram(educationalProgramBack);
        assertThat(group.getEducationalProgram()).isEqualTo(educationalProgramBack);

        group.educationalProgram(null);
        assertThat(group.getEducationalProgram()).isNull();
    }
}
