package com.example.campv.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object RegisterCollege : Screen("register_college")
    data object ForgotPassword : Screen("forgot_password")

    // Student Routes
    data object StudentDashboard : Screen("student_dashboard")
    data object Profile : Screen("profile")
    data object CreateDemand : Screen("create_demand")
    data object DemandDetails : Screen("demand_details/{demandId}") {
        fun createRoute(demandId: String) = "demand_details/$demandId"
    }
    data object Notifications : Screen("notifications")

    // Admin Routes
    data object AdminDashboard : Screen("admin_dashboard")
    data object ManageDemands : Screen("manage_demands")
    data object AdminDemandDetails : Screen("admin_demand_details/{demandId}") {
        fun createRoute(demandId: String) = "admin_demand_details/$demandId"
    }
    data object Reports : Screen("reports")
    data object Analytics : Screen("analytics")
    data object AdminProfile : Screen("admin_profile")

    // Principal Routes
    data object PrincipalDashboard : Screen("principal_dashboard")
    data object ManageStudents : Screen("manage_students")
    data object ManageAdmins : Screen("manage_admins")
    data object Departments : Screen("departments")
    data object CollegeAnalytics : Screen("college_analytics")
    data object CollegeSettings : Screen("college_settings")
    data object Broadcast : Screen("broadcast")

    // Platform Owner Routes
    data object PlatformDashboard : Screen("platform_dashboard")
    data object CollegeApproval : Screen("college_approval")
    data object PlatformAnalytics : Screen("platform_analytics")
    data object PlatformSettings : Screen("platform_settings")
}
