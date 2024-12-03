import BannerComponent from '@/components/screens/homeScreen/banner';
import CoursesComponent from '@/components/screens/homeScreen/courses';
import FooterComponent from '@/components/screens/homeScreen/footer';
import HeaderComponent from '@/components/screens/homeScreen/header';
import HomeScreen from '@/components/screens/homeScreen/homePage';
import SectionComponent from '@/components/screens/homeScreen/section';
import LoginScreen from '@/components/screens/login/loginScreen';
import DashboardScreen from '@/components/screens/mainPage/dashboardScreen';
import ChangePassword from '@/components/screens/password/changePassword';
import ResetPassword from '@/components/screens/password/resetPassword';
import SignUpScreen from '@/components/screens/signUp/signUpScreen';

import EditProfileScreen from '@/components/screens/profileScreen/editProfileScreen';
import CourseRegistrationScreen from '@/components/screens/course/courseRegistrationScreen';
import PaymentScreen from '@/components/screens/payment/paymentScreen';
import ScheduleScreen from '@/components/screens/schedule/scheduleScreen';
import TeacherScheduleScreen from '@/components/screens/schedule/teacherScheduleScreen';
import TeacherClassesScreen from '@/components/screens/class/teacherClassesScreen';
import StudentClassesScreen from '@/components/screens/class/studentClassesScreen';
import SelectSessionScreen from '@/components/screens/session/selectSessionScreen';
import AssignmentDetailScreen from '@/components/screens/assignment/assignmentDetailScreen';
import TeacherClassDetailScreen from '@/components/screens/class/teacherClassDetailScreen';
import AddAssignmentScreen from '@/components/screens/assignment/addAssignmentScreen';
import 'react-native-gesture-handler';
import React from 'react';
import BillScreen from '@/components/screens/bill/billScreen';
import ElectroBill from '@/components/screens/bill/electroBill';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import TeacherClassesExamScreen from '@/components/screens/exam/teacherClasssExamScreen';
import TeacherClassExamDetailScreen from '@/components/screens/exam/teacherClassExamDetailScreen';
import AddExamScreen from '@/components/screens/test/addExamScreen';
type RootStackParamList = {
  HomeScreen: undefined;
  LoginScreen:undefined;
  SignUpScreen:undefined;
  DashboardScreen:undefined;
  ChangePassword: undefined;
  ResetPassword:undefined;
  EditProfileScreen:undefined;
  CourseRegistrationScreen:undefined;
  PaymentScreen:undefined;
  ScheduleScreen: undefined;
  StudentClassesScreen:undefined;
  TeacherScheduleScreen:undefined;
  TeacherClassesScreen:undefined;
  TeacherClassDetailScreen:undefined;
  SelectSessionScreen:undefined;
  AssignmentDetailScreen:undefined;
  AddAssignmentScreen:undefined;
  BillScreen:undefined;
  ElectroBill:undefined;
  TeacherClassesExamScreen:undefined;
  TeacherClassExamDetailScreen:undefined;
  AddExamScreen:undefined;
};
const Stack = createNativeStackNavigator<RootStackParamList>();
export default function App() {
  return (
    <NavigationContainer independent={true} >
    <Stack.Navigator initialRouteName= "HomeScreen" screenOptions={{ headerShown: false }}>
      <Stack.Screen name="HomeScreen" component={HomeScreen} />
      <Stack.Screen name="LoginScreen" component={LoginScreen} />
      <Stack.Screen name="SignUpScreen" component={SignUpScreen} />
      <Stack.Screen name="DashboardScreen" component={DashboardScreen} />
      <Stack.Screen name="ChangePassword" component={ChangePassword} />
      <Stack.Screen name="ResetPassword" component={ResetPassword} />
      <Stack.Screen name="EditProfileScreen" component={EditProfileScreen} />
      <Stack.Screen name="CourseRegistrationScreen" component={CourseRegistrationScreen} />
      <Stack.Screen name="PaymentScreen" component={PaymentScreen} />
      <Stack.Screen name="ScheduleScreen" component={ScheduleScreen} />
      <Stack.Screen name="StudentClassesScreen" component={StudentClassesScreen} />
      <Stack.Screen name="TeacherScheduleScreen" component={TeacherScheduleScreen} />
      <Stack.Screen name="TeacherClassesScreen" component={TeacherClassesScreen} />
      <Stack.Screen name="TeacherClassDetailScreen" component={TeacherClassDetailScreen} />
      <Stack.Screen name="SelectSessionScreen" component={SelectSessionScreen} />
      <Stack.Screen name="AssignmentDetailScreen" component={AssignmentDetailScreen} />
      <Stack.Screen name="AddAssignmentScreen" component={AddAssignmentScreen} />
      <Stack.Screen name="BillScreen" component={BillScreen} />
      <Stack.Screen name="TeacherClassesExamScreen" component={TeacherClassesExamScreen} />
      <Stack.Screen name="TeacherClassExamDetailScreen" component={TeacherClassExamDetailScreen} />
      <Stack.Screen name="AddExamScreen" component={AddExamScreen} />

      
      
    </Stack.Navigator>
  </NavigationContainer>
  
  );
}


