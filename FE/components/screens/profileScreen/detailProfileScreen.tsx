import { View, Text, TouchableOpacity, StyleSheet, ScrollView, Image, Modal } from 'react-native';
import React, { useState } from 'react';
import { FontAwesome, Ionicons, MaterialIcons, Entypo,Feather,AntDesign } from '@expo/vector-icons';  

export default function UpdateProfileScreen({navigation}: {navigation: any}) {
    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.sectionTitle}>Thông tin cá nhân</Text>
            </View>
            <View style={styles.optionList}>
                <TouchableOpacity style={styles.option}>
                    <View style={styles.optionRow}>
                    <MaterialIcons name="update" size={24} color="green" />
                        <Text style={styles.optionText}>Cập nhật thông tin cá nhân</Text>
                    </View>
                </TouchableOpacity>

                <TouchableOpacity style={styles.option}
                onPress={() =>navigation.navigate('ChangePassword')}>
                    <View style={styles.optionRow}>
                    <MaterialIcons name="password" size={24} color="red" />
                        <Text style={styles.optionText}>Đổi mật khẩu</Text>
                    </View>
                </TouchableOpacity>

               
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
    },
    header: {
        flexDirection: 'column',
        alignItems: 'center',
        padding: 15,
        backgroundColor: '#fff',
        justifyContent: 'center',
    },
    sectionTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        marginBottom: 8,
        color: '#00bf63',
        textAlign: 'center',
    },
    optionList: {
        marginTop: 20,
    },
    option: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        padding: 20,
        backgroundColor: '#fff',
        borderWidth: 1,
        borderColor: '#ccc',
        borderRadius: 10,
        marginTop: 10,
        width: '90%',
        alignSelf: 'center',
    },
    optionRow: {
        flexDirection: 'row',
        alignItems: 'center',
    },
    optionText: {
        marginLeft: 10,
        fontSize: 16,
    },
});
