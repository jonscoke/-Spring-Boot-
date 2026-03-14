package com.youthhealth.modules.user.mapper;

import com.youthhealth.modules.user.entity.UserProfile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserProfileMapper {

    @Select("""
            SELECT id, user_id, gender, birthday, height_cm, weight_kg, activity_level, goal_type,
                   bmi, bmr, daily_calorie_target, created_at, updated_at
            FROM user_profile
            WHERE user_id = #{userId}
            """)
    UserProfile findByUserId(Long userId);

    @Insert("""
            INSERT INTO user_profile(user_id, gender, birthday, height_cm, weight_kg, activity_level, goal_type,
                                     bmi, bmr, daily_calorie_target)
            VALUES(#{userId}, #{gender}, #{birthday}, #{heightCm}, #{weightKg}, #{activityLevel}, #{goalType},
                   #{bmi}, #{bmr}, #{dailyCalorieTarget})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserProfile profile);

    @Update("""
            UPDATE user_profile
            SET gender = #{gender},
                birthday = #{birthday},
                height_cm = #{heightCm},
                weight_kg = #{weightKg},
                activity_level = #{activityLevel},
                goal_type = #{goalType},
                bmi = #{bmi},
                bmr = #{bmr},
                daily_calorie_target = #{dailyCalorieTarget}
            WHERE user_id = #{userId}
            """)
    int updateByUserId(UserProfile profile);
}
