package com.pugachev85.b_s_m.domain;

import static com.pugachev85.b_s_m.domain.AcademicYearTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.pugachev85.b_s_m.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AcademicYearTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AcademicYear.class);
        AcademicYear academicYear1 = getAcademicYearSample1();
        AcademicYear academicYear2 = new AcademicYear();
        assertThat(academicYear1).isNotEqualTo(academicYear2);

        academicYear2.setId(academicYear1.getId());
        assertThat(academicYear1).isEqualTo(academicYear2);

        academicYear2 = getAcademicYearSample2();
        assertThat(academicYear1).isNotEqualTo(academicYear2);
    }
}
