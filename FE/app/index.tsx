import HomeScreen from "@/components/screens/homeScreen/homeScreen";
import LoginScreen from "@/components/screens/loginScreen/login";
import TermsAndConditions from "@/components/screens/terms/terms";
import ExerciseScreen from "@/components/screens/exercise/exScreen";
import CourseInfoScreen from "@/components/screens/mainPage/courseInforScreen";
import DashboardScreen from "@/components/screens/mainPage/dashBoardScreen";
import Authentication from "@/components/screens/signUpScreen/Authentication";
import CreateAccount from "@/components/screens/signUpScreen/createAccount";
import FillUpInformation from "@/components/screens/signUpScreen/fillUpInformation";
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import Icon from 'react-native-vector-icons/Ionicons'; 
import { SafeAreaView } from "react-native";
import PracticeScreen from "@/components/screens/mainPage/practiceScreen";
import NewsFeedScreen from "@/components/screens/mainPage/newsFeedScreen";
import ForumScreen from "@/components/screens/mainPage/forumScreen";
import { createMaterialTopTabNavigator } from '@react-navigation/material-top-tabs';
import { Animated } from 'react-native'; 
import LessonDetailScreen from "@/components/screens/detailScreen/lessonDetailScreen";
import ScoreBoardScreen from "@/components/screens/detailScreen/scoreBoardScreen";
import ListEXScreen from "@/components/screens/exercise/listEXScreen";
import LessionListScreen from "@/components/screens/exercise/lessionScreen";
import UserProfileScreen from "@/components/screens/profileScreen/userProfileScreen";
import UpdateProfileScreen from "@/components/screens/profileScreen/updateProfileScreen";
import RequestFogetPassword from "@/components/screens/resetPassword/requestFogetPassword";
import ChangePassword from "@/components/screens/changePassword/changePassword";
import AuthenticationForgetPassword from "@/components/screens/resetPassword/authenticationForgetPassword";
import DetailProfileScreen from "@/components/screens/profileScreen/detaiProfileScreen";
import UserInfoScreen from "@/components/screens/profileScreen/userInfoScreen";
import ResetPassword from "@/components/screens/resetPassword/resetPassword";

type RootStackParamList = {
  Home: undefined; 
  LoginScreen: undefined;
  CreateAccount: undefined;
  FillUpInformation: undefined;
  Authentication: undefined;
  AuthenticationForgetPassword: undefined;
  MainTabs: undefined;
  SecondTab :undefined;
  LessonDetailScreen: undefined;
  ScoreBoardScreen: undefined;
  ListEXScreen: undefined;
  LessionListScreen : undefined;
  ExerciseScreen: undefined;
  UserProfileScreen: undefined;
  UpdateProfileScreen:undefined;
  RequestFogetPassword: undefined;
  ChangePassword:undefined;
  DetailProfileScreen: undefined;
  UserInfoScreen:undefined;
  ResetPassword:undefined;
};

const Stack = createNativeStackNavigator<RootStackParamList>();
const TabNavigator = createBottomTabNavigator();
const TopTabNavigator = createMaterialTopTabNavigator();

function MainTabs() {
  return (
    <TabNavigator.Navigator
      screenOptions={({ route }) => ({
        headerShown: false, 
        tabBarIcon: ({ focused, color, size }) => {
          let iconName: string = ''; 

          if (route.name === "Trang chủ") {
            iconName = focused ? "home" : "home-outline";
          } else if (route.name === "Thông tin") {
            iconName = focused ? "file-tray-full" : "file-tray-full-outline";
          } else if (route.name === "Luyện tập") {
            iconName = focused ? "briefcase" : "briefcase-outline";
          } else if (route.name === "Bảng tin") {
            iconName = focused ? "notifications" : "notifications-outline";
          }
          return <Icon name={iconName} size={size} color={color} />;
        },
        tabBarActiveTintColor: 'green', 
        tabBarInactiveTintColor: 'gray',
      })}
    >
      <TabNavigator.Screen name="Trang chủ" component={DashboardScreen} />
      <TabNavigator.Screen name="Luyện tập" component={PracticeScreen} />
      <TabNavigator.Screen name="Thông tin" component={CourseInfoScreen} />
      <TabNavigator.Screen name="Bảng tin" component={SecondTab} />
    </TabNavigator.Navigator>
  );
}

function SecondTab() {
  return (
    <TopTabNavigator.Navigator
      screenOptions={({ route }) => ({
        tabBarIcon: ({ focused, color }) => {
          let iconName: string = '';
          let size = 20;
          if (route.name === "Tin Tức") {
            iconName = focused ? "newspaper" : "newspaper-outline";
          } else if (route.name === "Diễn Đàn") {
            iconName = focused ? "chatbubbles" : "chatbubbles-outline";
          }

          return <Icon name={iconName} size={size} color={color} />;
        },
        tabBarActiveTintColor: 'green',
        tabBarInactiveTintColor: 'gray',
        tabBarLabelStyle: { fontSize: 10 },
        tabBarIndicatorStyle: { backgroundColor: 'green' }, 
      })}
    >
      <TopTabNavigator.Screen name="Tin Tức" component={NewsFeedScreen} />
      <TopTabNavigator.Screen name="Diễn Đàn" component={ForumScreen} />
    </TopTabNavigator.Navigator>
  );
}


export default function Router() {
  return (
    <NavigationContainer independent={true}>
      <Stack.Navigator screenOptions={{ headerShown: false }}>
        <Stack.Screen name="Home" component={HomeScreen} />
        <Stack.Screen name="LoginScreen" component={LoginScreen} />
        <Stack.Screen name="CreateAccount" component={CreateAccount} />
        <Stack.Screen name="FillUpInformation" component={FillUpInformation} />
        <Stack.Screen name="Authentication" component={Authentication} />
        <Stack.Screen name="AuthenticationForgetPassword" component={AuthenticationForgetPassword} />
        <Stack.Screen name="MainTabs" component={MainTabs}  />
        <Stack.Screen name="SecondTab" component={SecondTab} />
        <Stack.Screen name="LessonDetailScreen" component={LessonDetailScreen} />
        <Stack.Screen name="ScoreBoardScreen" component={ScoreBoardScreen} />
        <Stack.Screen name="ListEXScreen" component={ListEXScreen} />
        <Stack.Screen name="LessionListScreen" component={LessionListScreen} />
        <Stack.Screen name="ExerciseScreen" component={ExerciseScreen} />
        <Stack.Screen name="UserProfileScreen" component={UserProfileScreen} />
        <Stack.Screen name="UpdateProfileScreen" component={UpdateProfileScreen} />
        <Stack.Screen name="RequestFogetPassword" component={RequestFogetPassword} />
        <Stack.Screen name="ChangePassword" component={ChangePassword} />
        <Stack.Screen name="DetailProfileScreen" component={DetailProfileScreen} />
        <Stack.Screen name="UserInfoScreen" component={UserInfoScreen} />
        <Stack.Screen name="ResetPassword" component={ResetPassword} />


        

      </Stack.Navigator>
    </NavigationContainer>
    // <SafeAreaView>
    //   <LessonDetailScreen></LessonDetailScreen>
        
    // </SafeAreaView>
  );
}


