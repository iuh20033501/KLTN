import BannerComponent from '@/components/screens/homeScreen/banner';
import CoursesComponent from '@/components/screens/homeScreen/courses';
import FooterComponent from '@/components/screens/homeScreen/footer';
import HeaderComponent from '@/components/screens/homeScreen/header';
import SectionComponent from '@/components/screens/homeScreen/section';
import { Image, StyleSheet, Platform, View, ScrollView } from 'react-native';



export default function HomeScreen({navigation}: {navigation: any}) {
  return (
   <ScrollView>
      <HeaderComponent navigation={navigation}></HeaderComponent>
      <BannerComponent  navigation={navigation}></BannerComponent>
      <SectionComponent></SectionComponent>
      <CoursesComponent></CoursesComponent>
      <FooterComponent></FooterComponent>
   </ScrollView>
  );
}

const styles = StyleSheet.create({
  titleContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  stepContainer: {
    gap: 8,
    marginBottom: 8,
  },
  reactLogo: {
    height: 178,
    width: 290,
    bottom: 0,
    left: 0,
    position: 'absolute',
  },
});