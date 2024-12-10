import React, { useCallback, useEffect, useState } from 'react';
import { View, Text, StyleSheet, ActivityIndicator } from 'react-native';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useFocusEffect } from '@react-navigation/native';

const WeeklyExamSchedule = () => {
    const [totalExams, setTotalExams] = useState<number | null>(null);
    const [isLoading, setIsLoading] = useState(false);

    const calculateStartOfWeek = (date: Date) => {
        const selectedDay = new Date(date);
        const day = selectedDay.getDay();
        const diff = selectedDay.getDate() - day + (day === 0 ? -6 : 1);
        return new Date(selectedDay.setDate(diff));
    };

    const fetchWeeklyExams = async () => {
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
            const userId = profileResponse.data.u.idUser;
            const classesResponse = await http.get(`hocvien/getByHV/${userId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            const enrolledClasses = classesResponse.data;
            const examsPromises = enrolledClasses.map((classInfo: { idLopHoc: number }) =>
                http.get(`baitest/getBaiTestofLopTrue/${classInfo.idLopHoc}`, {
                    headers: { Authorization: `Bearer ${token}` },
                })
            );
            const examsResponses = await Promise.all(examsPromises);
            const allExams = examsResponses.flatMap((response) => response.data);
            const startOfWeek = calculateStartOfWeek(new Date());
            const endOfWeek = new Date(startOfWeek);
            endOfWeek.setDate(startOfWeek.getDate() + 6);
            const weeklyExams = allExams.filter((exam: { ngayBD: string }) => {
                const examDate = new Date(exam.ngayBD);
                return examDate >= startOfWeek && examDate <= endOfWeek;
            });

            setTotalExams(weeklyExams.length);
        } catch (error) {
            console.error('Failed to fetch weekly exams:', error);
        } finally {
            setIsLoading(false);
        }
    };

    useFocusEffect(
        useCallback(() => {
            fetchWeeklyExams();
        }, [])
    );

    return (
        <View style={styles.container}>
            {isLoading ? (
                <ActivityIndicator size="large" color="#00405d" />
            ) : (
                <Text style={styles.totalText}>{totalExams}</Text>
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

export default WeeklyExamSchedule;