package com.example.campv.core.navigation

sealed class NavigationDestination(val route: String) {
    data object Splash : NavigationDestination("splash")
    data object Login : NavigationDestination("login")
    data object RegisterCollege : NavigationDestination("register_college")
    data object ForgotPassword : NavigationDestination("forgot_password")

    // Student
    data object StudentDashboard : NavigationDestination("student_dashboard")
    data object Profile : NavigationDestination("profile")
    data object CreateDemand : NavigationDestination("create_demand")
    data object DemandDetails : NavigationDestination("demand_details/{demandId}") {
        fun createRoute(demandId: String) = "demand_details/$demandId"
    }
    data object Notifications : NavigationDestination("notifications")

    // Admin
    data object AdminDashboard : NavigationDestination("admin_dashboard")
    data object ManageDemands : NavigationDestination("manage_demands")
    data object Reports : NavigationDestination("reports")

    // Principal
    data object PrincipalDashboard : NavigationDestination("principal_dashboard")
    data object ManageStudents : NavigationDestination("manage_students")
    data object ManageAdmins : NavigationDestination("manage_admins")
    data object Departments : NavigationDestination("departments")
    data object CollegeAnalytics : NavigationDestination("college_analytics")
    data object CollegeSettings : NavigationDestination("college_settings")
    data object Broadcast : NavigationDestination("broadcast")

    // Platform Owner
    data object PlatformDashboard : NavigationDestination("platform_dashboard")
    data object CollegeApproval : NavigationDestination("college_approval")
    data object PlatformAnalytics : NavigationDestination("platform_analytics")
    data object PlatformSettings : NavigationDestination("platform_settings")
}
