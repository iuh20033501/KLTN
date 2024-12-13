import React, { useCallback, useEffect, useState } from 'react';
import { View, Text, StyleSheet, ActivityIndicator } from 'react-native';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useFocusEffect } from '@react-navigation/native';

const WeeklyScheduleTotal = () => {
    const [totalClasses, setTotalClasses] = useState<number | null>(null);
    const [isLoading, setIsLoading] = useState(false);

    const calculateStartOfWeek = (date: Date) => {
        const selectedDay = new Date(date);
        const day = selectedDay.getDay();
        const diff = selectedDay.getDate() - day + (day === 0 ? -6 : 1);
        return new Date(selectedDay.setDate(diff));
    };

    const fetchWeeklyTotalClasses = async () => {
        setIsLoading(true);
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                console.error('No token found');
                return;
            }
                const profileResponse = await http.get('auth/profile', {
                headers: { Authorization: `Bearer ${token}` },
            });
            const id = profileResponse.data.u.idUser;
            const classesResponse = await http.get(`hocvien/getByHV/${id}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            const enrolledClasses = classesResponse.data.filter(
                (classInfo: { trangThai: string }) => classInfo.trangThai === "FULL"
            );
            const schedulesPromises = enrolledClasses.map((classInfo: { idLopHoc: number }) =>
                http.get(`buoihoc/getbuoiHocByLop/${classInfo.idLopHoc}`, {
                    headers: { Authorization: `Bearer ${token}` },
                })
            );
            const schedulesResponses = await Promise.all(schedulesPromises);
            const allSchedules = schedulesResponses.flatMap((response) => response.data);
            const startOfWeek = calculateStartOfWeek(new Date());
            const endOfWeek = new Date(startOfWeek);
            endOfWeek.setDate(startOfWeek.getDate() + 6);
            const weeklySchedules = allSchedules.filter((schedule: { ngayHoc: string }) => {
                const scheduleDate = new Date(schedule.ngayHoc);
                return scheduleDate >= startOfWeek && scheduleDate <= endOfWeek;
            });
            setTotalClasses(weeklySchedules.length);
        } catch (error) {
            console.error('Failed to fetch weekly total classes:', error);
        } finally {
            setIsLoading(false);
        }
    };
    useFocusEffect(
        useCallback(() => {
            fetchWeeklyTotalClasses();
        }, [])
    );

    return (
        <View style={styles.container}>
            {isLoading ? (
                <ActivityIndicator size="large" color="#00405d" />
            ) : (
                <Text style={styles.totalText}>{totalClasses}</Text>
            )}
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#f8f9fa',
    },
    totalText: {
        fontSize: 20,
        fontWeight: 'bold',
        color: 'black',
    },
});

export default WeeklyScheduleTotal;
