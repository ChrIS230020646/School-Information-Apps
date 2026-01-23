package com.example.comp_3132sef.data.remote

import com.squareup.moshi.Json

data class SchoolDto(
    @Json(name = "SCHOOL NO.")
    val schoolNo: String?,

    @Json(name = "ENGLISH NAME")
    val englishName: String?,

    @Json(name = "中文名稱")
    val chineseName: String?,

    @Json(name = "ENGLISH CATEGORY")
    val englishCategory: String?,

    @Json(name = "中文類別")
    val chineseCategory: String?,

    @Json(name = "ENGLISH ADDRESS")
    val englishAddress: String?,

    @Json(name = "中文地址")
    val chineseAddress: String?,

    @Json(name = "LATITUDE")
    val latitude: Double?,

    @Json(name = "LONGITUDE")
    val longitude: Double?,

    @Json(name = "DISTRICT")
    val district: String?,

    @Json(name = "分區")
    val chineseDistrict: String?,

    @Json(name = "TELEPHONE")
    val telephone: String?,

    @Json(name = "聯絡電話")
    val chineseTelephone: String?,

    @Json(name = "FAX NUMBER")
    val faxNumber: String?,

    @Json(name = "傳真號碼")
    val chineseFaxNumber: String?,

    @Json(name = "WEBSITE")
    val website: String?,

    @Json(name = "網頁")
    val chineseWebsite: String?,

    @Json(name = "RELIGION")
    val religion: String?,

    @Json(name = "宗教")
    val chineseReligion: String?,

    @Json(name = "STUDENTS GENDER")
    val studentsGender: String?,

    @Json(name = "就讀學生性別")
    val chineseStudentsGender: String?,

    @Json(name = "SESSION")
    val session: String?,

    @Json(name = "學校授課時間")
    val chineseSession: String?,

    @Json(name = "FINANCE TYPE")
    val financeType: String?,

    @Json(name = "資助種類")
    val chineseFinanceType: String?,

    @Json(name = "SCHOOL LEVEL")
    val schoolLevel: String?,

    @Json(name = "學校類型")
    val chineseSchoolLevel: String?
)