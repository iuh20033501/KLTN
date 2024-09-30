import HomeScreen from "@/components/screens/homeScreen/homeScreen";
import LoginScreen from "@/components/screens/LoginScreen/login";
import TermsAndConditions from "@/components/screens/terms/terms";
import ExerciseScreen from "@/components/screens/exercise/exScreen";
import CourseInfoScreen from "@/components/screens/MainPage/courseInforScreen";
import DashboardScreen from "@/components/screens/MainPage/dashBoardScreen";
import Authentication from "@/components/screens/SignUpScreen/Authentication";
import CreateAccount from "@/components/screens/SignUpScreen/createAccount";
import FillUpInformation from "@/components/screens/SignUpScreen/fillUpInformation";
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import Icon from 'react-native-vector-icons/Ionicons'; 
import ForgetPassword from "@/components/screens/resetPassword/forgetPassword";
import AuthenticationPass from "@/components/screens/resetPassword/AuthenticationPass";
import { SafeAreaView } from "react-native";
import PracticeScreen from "@/components/screens/MainPage/practiceScreen";

type RootStackParamList = {
  Home: undefined; 
  LoginScreen: undefined;
  CreateAccount: undefined;
  FillUpInformation: undefined;
  Authentication: undefined;
  ForgetPassword: undefined;
  AuthenticationPass: undefined;
  MainTabs: undefined;
};

const Stack = createNativeStackNavigator<RootStackParamList>();
const TabNavigator = createBottomTabNavigator();

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
          }
          else if (route.name === "Luyện tập") {
            iconName = focused ? "briefcase" : "briefcase-outline";
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
    </TabNavigator.Navigator>
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
        <Stack.Screen name="AuthenticationPass" component={AuthenticationPass} />
        <Stack.Screen name="ForgetPassword" component={ForgetPassword} />
        <Stack.Screen name="MainTabs" component={MainTabs} />
      </Stack.Navigator>
    </NavigationContainer>
    // <SafeAreaView>
    //   <PracticeScreen></PracticeScreen>
    // </SafeAreaView>
  );
}


