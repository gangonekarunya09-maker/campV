package com.example.campv.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.campv.core.constants.AppConstants
import com.example.campv.feature.admin.ui.AdminDashboardScreen
import com.example.campv.feature.admin.ui.ManageDemandsScreen
import com.example.campv.feature.admin.ui.ReportsScreen
import com.example.campv.feature.auth.ui.ForgotPasswordScreen
import com.example.campv.feature.auth.ui.LoginScreen
import com.example.campv.feature.auth.ui.RegisterCollegeScreen
import com.example.campv.feature.platformowner.ui.CollegeApprovalScreen
import com.example.campv.feature.platformowner.ui.PlatformAnalyticsScreen
import com.example.campv.feature.platformowner.ui.PlatformDashboardScreen
import com.example.campv.feature.platformowner.ui.PlatformSettingsScreen
import com.example.campv.feature.principal.ui.BroadcastScreen
import com.example.campv.feature.principal.ui.CollegeAnalyticsScreen
import com.example.campv.feature.principal.ui.CollegeSettingsScreen
import com.example.campv.feature.principal.ui.DepartmentsScreen
import com.example.campv.feature.principal.ui.ManageAdminsScreen
import com.example.campv.feature.principal.ui.ManageStudentsScreen
import com.example.campv.feature.principal.ui.PrincipalDashboardScreen
import com.example.campv.feature.splash.ui.SplashScreen
import com.example.campv.feature.student.ui.CreateDemandScreen
import com.example.campv.feature.student.ui.DemandDetailsScreen
import com.example.campv.feature.student.ui.NotificationsScreen
import com.example.campv.feature.student.ui.ProfileScreen
import com.example.campv.feature.student.ui.StudentDashboardScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // Splash
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Auth
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { role ->
                    val destination = when (role) {
                        AppConstants.ROLE_ADMIN -> Screen.AdminDashboard.route
                        AppConstants.ROLE_PRINCIPAL -> Screen.PrincipalDashboard.route
                        AppConstants.ROLE_PLATFORM_OWNER -> Screen.PlatformDashboard.route
                        else -> Screen.StudentDashboard.route
                    }
                    navController.navigate(destination) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterCollegeClick = {
                    navController.navigate(Screen.RegisterCollege.route)
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }

        composable(Screen.RegisterCollege.route) {
            RegisterCollegeScreen(
                onBackClick = { navController.popBackStack() },
                onSubmitSuccess = { navController.popBackStack() }
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackClick = { navController.popBackStack() },
                onEmailSent = { navController.popBackStack() }
            )
        }

        // Student Flow
        composable(Screen.StudentDashboard.route) {
            StudentDashboardScreen(
                onCreateDemandClick = { navController.navigate(Screen.CreateDemand.route) },
                onDemandClick = { demandId ->
                    navController.navigate(Screen.DemandDetails.createRoute(demandId))
                },
                onProfileClick = { navController.navigate(Screen.Profile.route) },
                onNotificationsClick = { navController.navigate(Screen.Notifications.route) }
            )
        }

        composable(Screen.CreateDemand.route) {
            CreateDemandScreen(
                onBackClick = { navController.popBackStack() },
                onDemandCreated = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.DemandDetails.route,
            arguments = listOf(navArgument("demandId") { type = NavType.StringType })
        ) { backStackEntry ->
            val demandId = backStackEntry.arguments?.getString("demandId") ?: ""
            DemandDetailsScreen(
                demandId = demandId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onBackClick = { navController.popBackStack() },
                onLoggedOut = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Notifications.route) {
            NotificationsScreen(onBackClick = { navController.popBackStack() })
        }

        // Admin Flow
        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(
                onManageDemandsClick = { navController.navigate(Screen.ManageDemands.route) },
                onReportsClick = { navController.navigate(Screen.Reports.route) }
            )
        }

        composable(Screen.ManageDemands.route) {
            ManageDemandsScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.Reports.route) {
            ReportsScreen(onBackClick = { navController.popBackStack() })
        }

        // Principal Flow
        composable(Screen.PrincipalDashboard.route) {
            PrincipalDashboardScreen(
                onManageStudentsClick = { navController.navigate(Screen.ManageStudents.route) },
                onManageAdminsClick = { navController.navigate(Screen.ManageAdmins.route) },
                onDepartmentsClick = { navController.navigate(Screen.Departments.route) },
                onCollegeAnalyticsClick = { navController.navigate(Screen.CollegeAnalytics.route) },
                onCollegeSettingsClick = { navController.navigate(Screen.CollegeSettings.route) },
                onBroadcastClick = { navController.navigate(Screen.Broadcast.route) }
            )
        }

        composable(Screen.ManageStudents.route) {
            ManageStudentsScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.ManageAdmins.route) {
            ManageAdminsScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.Departments.route) {
            DepartmentsScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.CollegeAnalytics.route) {
            CollegeAnalyticsScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.CollegeSettings.route) {
            CollegeSettingsScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.Broadcast.route) {
            BroadcastScreen(onBackClick = { navController.popBackStack() })
        }

        // Platform Owner Flow
        composable(Screen.PlatformDashboard.route) {
            PlatformDashboardScreen(
                onCollegeApprovalClick = { navController.navigate(Screen.CollegeApproval.route) },
                onPlatformAnalyticsClick = { navController.navigate(Screen.PlatformAnalytics.route) },
                onPlatformSettingsClick = { navController.navigate(Screen.PlatformSettings.route) }
            )
        }

        composable(Screen.CollegeApproval.route) {
            CollegeApprovalScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.PlatformAnalytics.route) {
            PlatformAnalyticsScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.PlatformSettings.route) {
            PlatformSettingsScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
