object Constants {
    const val TABLE_EMP_ACCESS = "V1_EmpAccess"
    const val TABLE_MENU = "V1_Menu"
    const val TABLE_DASHBOARD = "V1_Dashboard"
    const val TABLE_PRODUCT_SALES = "V1_ProductSalesReport"
    const val TABLE_REGION_MASTER = "V1_RegionMaster"
    const val TABLE_TERRITORY_MASTER = "V1_TerritoryMaster"
    const val TABLE_DEALER_MASTER = "V1_DealerMaster"
    const val TABLE_FIN_YEAR_MASTER = "V1_FinYear"
    const val TABLE_FIN_MONTH_MASTER = "V1_FinMonth"
    const val TABLE_LEDGER_OPENING_BALANCE = "V1_LedgerOpeningBalance"
    const val TABLE_DEALER_LEDGER = "V1_DealerLedger"
    const val TABLE_JU_DOCTOR = "V1_JUDoctorItems"
    const val TABLE_PROMOTION_ACTIVITY_LIST = "PromotionActivityList"
    const val TABLE_PROMOTION_ACTIVITY_FIELDS = "PromotionActivityFields"
    const val TABLE_DISTRICT_LIST = "DistrictMaster"
    const val TABLE_VILLAGE_LIST = "VillageMaster"
    const val TABLE_PROMOTION_ACTIVITY_ENTRY = "PromotionActivityEntry"
    const val TABLE_PROMOTION_ACTIVITY_COUNT = "PromotionActivityCount"
    const val TABLE_PROMOTION_MOBILE_NUMBERS = "PromotionMobileNumbers"
    const val TABLE_CDO_FOCUS_PRODUCT = "FocusProductsSummary"
    const val TABLE_DEALER_LIQUIDATION = "V1_DealerLiquidation"
    const val TABLE_CONFIG = "Config"
    const val TABLE_LIQUIDATION_CONFIG = "DealerLiquidation"
    const val TABLE_LOGIN_INFO = "LoginInfo"

    const val FOLDER_PROMOTION_ENTRY_IMAGES = "PromotionEntryImages"

    const val EMP_ROLE_RM = "RM"
    const val EMP_ROLE_DM = "DM"
    const val EMP_ROLE_SO = "SO"
    const val EMP_ROLE_DL = "DL"
    const val EMP_ROLE_CDO = "CDO"

    const val APP_MODE_CDO = "CDO"
    const val APP_MODE_DEALER = "Dealer"
    const val APP_MODE_STAFF = "Staff"

    const val CURRENT_APP_MODE = APP_MODE_CDO

    const val HEADING_MENU_0001 = "H_001"
    const val HEADING_MENU_0002 = "H_002"
    const val HEADING_MENU_0003 = "H_003"

    const val SCREEN_DASHBOARD = "C_001"
    const val SCREEN_LEDGER = "C_002"
    const val SCREEN_ONLINE_ORDER = "C_003"
    const val SCREEN_YOUR_ORDERS = "C_004"
    const val SCREEN_JU_Doctor = "C_005"
    const val SCREEN_WEATHER = "C_006"
    const val SCREEN_PROFILE = "C_007"
    const val SCREEN_DEVICES = "C_008"
    const val SCREEN_PROMOTION_ENTRY = "C_009"
    const val SCREEN_PROMOTION_ENTRIES_LIST = "C_010"
    const val SCREEN_CDO_FOCUS_PRODUCT = "C_011"
    const val SCREEN_CDO_LIQUIDATION = "C_012"
    const val SCREEN_LOGIN_INFO = "C_013"


    const val FIELD_C_CODE = "ccode"
    const val FIELD_REG_CODE = "regcode"
    const val FIELD_EMP_Name = "emp_name"
    const val FIELD_T_CODE = "tcode"
    const val FIELD_ROLE_ID = "emp_role_id"
    const val FIELD_START_DATE = "start_date"
    const val FIELD_END_DATE = "end_date"
    const val FIELD_UPDATED_TIME = "updatedTime"
    const val FIELD_EVENT_ID = "actid"
    const val FIELD_STATE_CODE = "st_code"
    const val FIELD_DISTRICT_CODE = "district_id"

    const val ERROR_MSG = "Location not found.\nPlease turn on the GPS location and try again"

    const val Test = "[{\"Code\":\"Cereals\",\"Name\":\"Cereals\",\"Data\":[{\"Code\":\"Bajra\",\"Name\":\"Bajra\"},{\"Code\":\"Barley\",\"Name\":\"Barley\"},{\"Code\":\"Maize\",\"Name\":\"Maize\"},{\"Code\":\"Rice\",\"Name\":\"Rice\"},{\"Code\":\"Wheat\",\"Name\":\"Wheat\"}]},{\"Code\":\"Fiber\",\"Name\":\"Fiber\",\"Data\":[{\"Code\":\"Cotton\",\"Name\":\"Cotton\"},{\"Code\":\"Jute\",\"Name\":\"Jute\"},{\"Code\":\"Silk\",\"Name\":\"Silk\"}]},{\"Code\":\"Fodder \",\"Name\":\"Fodder \",\"Data\":[{\"Code\":\"Barseem\",\"Name\":\"Barseem\"},{\"Code\":\"Oat\",\"Name\":\"Oat\"},{\"Code\":\"Sorghum\",\"Name\":\"Sorghum\"},{\"Code\":\"Sudan Grass\",\"Name\":\"Sudan Grass\"}]},{\"Code\":\"Fruit\",\"Name\":\"Fruit\",\"Data\":[{\"Code\":\"Apple\",\"Name\":\"Apple\"},{\"Code\":\"Banana\",\"Name\":\"Banana\"},{\"Code\":\"Citrus\",\"Name\":\"Citrus\"},{\"Code\":\"Grapes\",\"Name\":\"Grapes\"},{\"Code\":\"Guava\",\"Name\":\"Guava\"},{\"Code\":\"Litchi\",\"Name\":\"Litchi\"},{\"Code\":\"Mango\",\"Name\":\"Mango\"},{\"Code\":\"Papaya\",\"Name\":\"Papaya\"},{\"Code\":\"Pine Apple\",\"Name\":\"Pine Apple\"},{\"Code\":\"Pomegranate\",\"Name\":\"Pomegranate\"},{\"Code\":\"Spota\",\"Name\":\"Spota\"},{\"Code\":\"Strawberry\",\"Name\":\"Strawberry\"}]},{\"Code\":\"Oil Crops\",\"Name\":\"Oil Crops\",\"Data\":[{\"Code\":\"Groundnut\",\"Name\":\"Groundnut\"},{\"Code\":\"Mustard\",\"Name\":\"Mustard\"},{\"Code\":\"Soybean\",\"Name\":\"Soybean\"},{\"Code\":\"Sunflower\",\"Name\":\"Sunflower\"},{\"Code\":\"Caster\",\"Name\":\"Caster\"},{\"Code\":\"Til\",\"Name\":\"Til\"},{\"Code\":\"Mentha/Mint\",\"Name\":\"Mentha/Mint\"}]},{\"Code\":\"Others\",\"Name\":\"Others\",\"Data\":[{\"Code\":\"Coffee\",\"Name\":\"Coffee\"},{\"Code\":\"Beetle wine\",\"Name\":\"Beetle wine\"},{\"Code\":\"Coconut\",\"Name\":\"Coconut\"},{\"Code\":\"Guar\",\"Name\":\"Guar\"},{\"Code\":\"Rubber\",\"Name\":\"Rubber\"},{\"Code\":\"Sugarcane\",\"Name\":\"Sugarcane\"},{\"Code\":\"Tea\",\"Name\":\"Tea\"},{\"Code\":\"Tobacco\",\"Name\":\"Tobacco\"},{\"Code\":\"Flowers\",\"Name\":\"Flowers\"}]},{\"Code\":\"Pulses\",\"Name\":\"Pulses\",\"Data\":[{\"Code\":\"Gram\",\"Name\":\"Gram\"},{\"Code\":\"Red Gram\",\"Name\":\"Red Gram\"},{\"Code\":\"Green Gram\",\"Name\":\"Green Gram\"},{\"Code\":\"Beans\",\"Name\":\"Beans\"}]},{\"Code\":\"Spices\",\"Name\":\"Spices\",\"Data\":[{\"Code\":\"Coriander\",\"Name\":\"Coriander\"},{\"Code\":\"Cardamom\",\"Name\":\"Cardamom\"},{\"Code\":\"Cumin\",\"Name\":\"Cumin\"},{\"Code\":\"Methi\",\"Name\":\"Methi\"},{\"Code\":\"Turmeric\",\"Name\":\"Turmeric\"}]},{\"Code\":\"Vegetables\",\"Name\":\"Vegetables\",\"Data\":[{\"Code\":\"Carrot\",\"Name\":\"Carrot\"},{\"Code\":\"Chili\",\"Name\":\"Chili\"},{\"Code\":\"Cucurbits\",\"Name\":\"Cucurbits\"},{\"Code\":\"Garlic\",\"Name\":\"Garlic\"},{\"Code\":\"Gourds\",\"Name\":\"Gourds\"},{\"Code\":\"Okra\",\"Name\":\"Okra\"},{\"Code\":\"Onion\",\"Name\":\"Onion\"},{\"Code\":\"Parval\",\"Name\":\"Parval\"},{\"Code\":\"Potato\",\"Name\":\"Potato\"},{\"Code\":\"Radish \",\"Name\":\"Radish \"},{\"Code\":\"Tomato \",\"Name\":\"Tomato \"},{\"Code\":\"Zinger\",\"Name\":\"Zinger\"},{\"Code\":\"Brinjal\",\"Name\":\"Brinjal\"},{\"Code\":\"Cabbage\",\"Name\":\"Cabbage\"},{\"Code\":\"Peas\",\"Name\":\"Peas\"}]}]"

    const val FType_Label = 1
    const val FType_TextBox = 2
    const val FType_Dropdown = 3
    const val FType_Calendar = 4
    const val FType_Checkbox = 5
    const val FType_Radiobutton = 6
    const val FType_Seekbar = 7
    const val FType_Switch = 8
    const val FType_Location = 9
    const val FType_isWhatsApp = 10
    const val FType_AreaDropdown = 12
    const val FType_ScrollGridView = 13
    const val FType_ExpandableRecyclerView = 14
    const val FType_CaptureImages = 15
    const val FType_AreaDropdownMultiple = 16
    const val FType_Buttons = 20

    const val DIVIDED_BY = 100000.0
}