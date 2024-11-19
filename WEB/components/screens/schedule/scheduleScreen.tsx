import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ImageBackground, Modal } from 'react-native';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { vi } from 'date-fns/locale';

interface ClassInfo {
    idLopHoc: number;
    tenLopHoc: string;
    trangThai: string
}

interface DaySchedule {
    chuDe: string;
    idBuoiHoc: number;
    ngayHoc: string;
    gioHoc: string;
    gioKetThuc: string;
    lopHoc: ClassInfo;
    noiHoc: string;
    hocOnl: boolean;
}

export default function ScheduleScreen({ navigation, route }: { navigation: any, route: any }) {
    const [selectedDate, setSelectedDate] = useState(new Date());
    const [weekSchedule, setWeekSchedule] = useState<DaySchedule[]>([]);
    const { idUser, nameUser } = route.params;
    const [isDatePickerOpen, setIsDatePickerOpen] = useState(false);

    const calculateStartOfWeek = (date: string | number | Date) => {
        const selectedDay = new Date(date);
        const day = selectedDay.getDay();
        const diff = selectedDay.getDate() - day + (day === 0 ? -6 : 1);
        return new Date(selectedDay.setDate(diff));
    };

    const fetchWeeklySchedule = async (startDate: Date) => {
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                console.error('No token found');
                return;
            }
            const response = await http.get(`buoihoc/getByHocVien/${idUser}`, {
                params: {
                    startDate: startDate.toISOString().split('T')[0],
                },
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            const fullClasses = response.data.filter((schedule: DaySchedule) => schedule.lopHoc.trangThai === "FULL");
            setWeekSchedule(fullClasses);
            console.log(response.data);
        } catch (error) {
            console.error('Failed to fetch weekly schedule:', error);
        }
    };

    useEffect(() => {
        const startOfWeek = calculateStartOfWeek(new Date());
        setSelectedDate(startOfWeek);
        fetchWeeklySchedule(startOfWeek);
    }, []);

    const handleDateChange = (date: Date | null) => {
        if (date) {
            const startOfWeek = calculateStartOfWeek(date);
            setSelectedDate(startOfWeek); 
            fetchWeeklySchedule(startOfWeek); 
            setIsDatePickerOpen(false); 
        }
    };

    const handleCurrentWeek = () => {
        const currentWeekStart = calculateStartOfWeek(new Date());
        setSelectedDate(currentWeekStart);
        fetchWeeklySchedule(currentWeekStart);
    };

    const getDaysOfWeek = (startOfWeek: Date) => {
        const days = [];
        for (let i = 0; i < 7; i++) {
            const day = new Date(startOfWeek);
            day.setDate(day.getDate() + i);
            days.push(day);
        }
        return days;
    };

    const daysOfWeek = getDaysOfWeek(selectedDate);
    const scheduleByDay = daysOfWeek.map((day) => {
        const dayString = day.toISOString().split('T')[0];
        return {
            date: day,
            classes: weekSchedule.filter((schedule) => schedule.ngayHoc.startsWith(dayString)),
        };
    });

    const handlePreviousWeek = () => {
        const newDate = new Date(selectedDate);
        newDate.setDate(newDate.getDate() - 7);
        setSelectedDate(newDate);
        fetchWeeklySchedule(newDate);
    };

    const handleNextWeek = () => {
        const newDate = new Date(selectedDate);
        newDate.setDate(newDate.getDate() + 7);
        setSelectedDate(newDate);
        fetchWeeklySchedule(newDate);
    };

    const toggleDatePicker = () => {
        setIsDatePickerOpen(!isDatePickerOpen);
    };

    return (
        <ImageBackground
            source={require('../../../image/bglogin.png')}
            style={styles.background}
            resizeMode="cover"
        >
            <View style={styles.overlayContainer}>
                <View style={styles.navigationButtons}>
                    <TouchableOpacity style={styles.backButton} onPress={() => navigation.navigate('DashboardScreen')}>
                        <Text style={styles.backButtonText}>Quay về</Text>
                    </TouchableOpacity>
                </View>
                <Text style={styles.title}>Lịch học trong tuần</Text>

                <View style={styles.datePickerContainer}>
                    <View style={styles.leftControls}>
                        <View style={styles.datePickerWrapperHeader}>
                            <TouchableOpacity style={styles.dateButton} onPress={() => setIsDatePickerOpen(true)}>
                                <Text style={styles.buttonText}>{selectedDate.toLocaleDateString('vi-VN')}</Text>
                            </TouchableOpacity>

                        </View>
                        <TouchableOpacity style={styles.backButton2} onPress={handleCurrentWeek}>
                            <Text style={styles.backButtonText2}>Hiện tại</Text>
                        </TouchableOpacity>
                    </View>

                    <View style={styles.rightControls}>
                        <TouchableOpacity style={styles.backButton2} onPress={handlePreviousWeek}>
                            <Text style={styles.backButtonText2}>Tuần trước</Text>
                        </TouchableOpacity>
                        <TouchableOpacity style={styles.backButton2} onPress={handleNextWeek}>
                            <Text style={styles.backButtonText2}>Tuần kế tiếp</Text>
                        </TouchableOpacity>
                    </View>
                </View>

                <View style={styles.scheduleTable}>
                    <View style={styles.row}>
                        {scheduleByDay.map((day, index) => (
                            <View key={index} style={styles.dayColumn}>
                                <Text style={styles.dayHeader}>
                                    {day.date.toLocaleDateString('vi-VN', { weekday: 'short', day: 'numeric', month: 'numeric' })}
                                </Text>
                                {day.classes.length > 0 ? (
                                    day.classes.map((classInfo) => (
                                        <View
                                            key={classInfo.idBuoiHoc}
                                            style={[
                                                styles.classBox,
                                                { backgroundColor: classInfo.hocOnl === true ? '#d0ebff' : '#f0f0f0' }
                                            ]}
                                        >
                                            <Text style={styles.textColumn}>Chủ đề: {classInfo.chuDe}</Text>
                                            <Text style={styles.textColumn}>Giờ: {classInfo.gioHoc} - {classInfo.gioKetThuc}</Text>
                                            <Text style={styles.textColumn}>Lớp: {classInfo.lopHoc.tenLopHoc}</Text>
                                            <Text style={styles.textColumn}>Phòng: {classInfo.noiHoc}</Text>
                                        </View>
                                    ))
                                ) : (
                                    <Text style={styles.noClass}>Không có lớp</Text>
                                )}
                            </View>
                        ))}
                    </View>
                </View>
                <View style={styles.legendContainer}>
                    <View style={[styles.legendBox, { backgroundColor: '#f0f0f0' }]} />
                    <Text style={styles.legendText}>Học trực tiếp</Text>
                    <View style={[styles.legendBox, { backgroundColor: '#d0ebff' }]} />
                    <Text style={styles.legendText}>Học trực tuyến</Text>
                </View>
                <Modal
                    visible={isDatePickerOpen}
                    transparent
                    animationType="fade"
                    onRequestClose={() => setIsDatePickerOpen(false)}
                >
                    <View style={styles.modalContainer}>
                        <View style={styles.datePickerWrapper}>
                            <DatePicker
                                selected={selectedDate}
                                onChange={handleDateChange}
                                inline
                                locale={vi} 
                                dateFormat="dd/MM/yyyy"

                            />
                             <TouchableOpacity style={styles.closeButton} onPress={() => setIsDatePickerOpen(false)}>
                            <Text style={styles.closeButtonText}>Đóng</Text>
                        </TouchableOpacity>
                        </View>
                       
                    </View>
                </Modal>
            </View>
        </ImageBackground>
    );
}

const styles = StyleSheet.create({
    background: {
        flex: 1,
        paddingHorizontal: 400,
        height: 990,
        paddingVertical: 200,

    },
    overlayContainer: {
        flex: 1,
        padding: 20,
        backgroundColor: 'rgba(255, 255, 255, 0.8)',
        borderRadius: 15,
        marginHorizontal: 20,
    },
    navigationButtons: {
        marginBottom: 10,
        flexDirection: 'row',
        justifyContent: 'flex-start',
    },
    datePickerContainer: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: 10,
    },
    leftControls: {
        flexDirection: 'row',
        alignItems: 'center',
        marginLeft: 65,
        marginBottom: 20
    },
    rightControls: {
        flexDirection: 'row',
        alignItems: 'center',
        marginRight: 65,
        marginBottom: 20
    },
    scheduleTable: {
        flexDirection: 'column',
        alignItems: 'center',
    },
    row: {
        flexDirection: 'row',
        justifyContent: 'space-around',
    },
    dayColumn: {
        width: 140,
        padding: 10,
        height:250,
        borderWidth: 1,
        borderColor: '#ddd',
        marginRight: 10,
        alignItems: 'center',
    },
    dayHeader: {
        fontWeight: 'bold',
        textAlign: 'center',
        marginBottom: 5,
    },
    classBox: {
        paddingVertical: 5,
        paddingHorizontal: 5,
        borderRadius: 5,
        backgroundColor: '#f0f0f0',
        marginBottom: 10,
        alignItems: 'flex-start',
    },
    noClass: {
        color: 'grey',
        fontStyle: 'italic',
    },
    backButton: {
        alignSelf: 'flex-start',
        padding: 10,
        backgroundColor: '#00405d',
        borderRadius: 5,
        marginBottom: 10,
    },
    backButtonText: {
        color: '#fff',
        fontWeight: 'bold',
    },
    backButton2: {
        padding: 10,
        backgroundColor: '#00405d',
        borderRadius: 5,
        marginRight: 15,
        marginLeft: 15
    },
    backButtonText2: {
        color: '#fff',
        fontWeight: 'bold',
        width: 100,
        textAlign: 'center'
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        color: '#00405d',
        textAlign: 'center',
        marginBottom: 20,
    },
    textColumn: {
        fontSize: 12,
        fontWeight: 'bold',
        color: '#00405d',
        marginBottom: 5,
        paddingBottom:10
    },
    legendContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
        marginTop: 20,
    },
    legendBox: {
        width: 20,
        height: 20,
        marginHorizontal: 5,
        borderRadius: 5,
        borderWidth: 1
    },
    legendText: {
        fontSize: 14,
        color: '#00405d',
        fontWeight: 'bold',
        marginRight: 15,
    },
    modalContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
    },
    datePickerWrapper: {
        padding: 20,
        borderRadius: 10,  
    },
    datePickerWrapperHeader: {
        width:200,
        padding: 20,
        borderRadius: 10,  
    },
    closeButton: {
        marginTop: 20,
        padding: 10,
        backgroundColor: '#00405d',
        borderRadius: 5,
    },
    closeButtonText: {
        color: '#fff',
        fontWeight: 'bold',
        textAlign:'center'
    },
    dateButton: {
        padding: 10,
        borderRadius: 5,
        alignItems: 'center',
        borderWidth: 1
    },
    buttonText: {
        color: 'black',
        fontWeight: 'bold',
    },
});
