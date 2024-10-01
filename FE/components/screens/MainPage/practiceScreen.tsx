import React from 'react';
import { View, Text, TouchableOpacity, Image, StyleSheet, ImageBackground } from 'react-native';

export default function PracticeScreen({navigation}: {navigation: any}) {
    const questIMG = require('../../../image/background/questIMG.jpg')
    const questIMG2 = require('../../../image/background/questIMG2.png')
    return (
        <View style={styles.container}>
            <Text style={styles.headerText}>Luyện tập</Text>
            <View style={{
                height: 170,
                borderRadius: 50,
                overflow: 'hidden'
            }}>
                <ImageBackground source={questIMG} style={[styles.card, { backgroundColor: '#D4F4C4' }]}>
                    <View style={styles.cardContent}>
                        <Text style={styles.cardTitle}>Bài tập theo kỹ năng</Text>
                        <TouchableOpacity style={[styles.button, { backgroundColor: '#47BA43' }]}
                        onPress={() => navigation.navigate('ListEXScreen')}>
                            <Text style={styles.buttonText}>Luyện ngay</Text>
                        </TouchableOpacity>
                    </View>

                </ImageBackground>

            </View>
            <View style={{
                height: 170,
                borderRadius: 50,
                overflow: 'hidden',
                marginTop:25
            }}>
                <ImageBackground source={questIMG2} style={[styles.card, { backgroundColor: '#FFE9AF' }]}>
                    <View style={styles.cardContent}>
                        <Text style={styles.cardTitle}>Thư viện video, audio</Text>
                        <TouchableOpacity style={[styles.button, { backgroundColor: '#FCA034' }]}>
                            <Text style={styles.buttonText}>Xem ngay</Text>
                        </TouchableOpacity>
                    </View>

                </ImageBackground>
            </View>

        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        padding: 20,
        backgroundColor: '#fff',
    },
    headerText: {
        fontSize: 20,
        fontWeight: 'bold',
        color: '#388E3C',
        textAlign: 'center',
        marginBottom: 20,
        height: 40
    },
    card: {
        borderRadius: 20,
        padding: 15,
        marginBottom: 20,
        alignItems: 'center',
        width: "auto",
        height: 170
    },
    cardContent: {
        flex: 1,
    },
    cardTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        marginBottom: 10,
        textAlign: "center"
    },
    button: {
        paddingVertical: 10,
        paddingHorizontal: 15,
        borderRadius: 20,
        justifyContent: 'center',
        marginTop: 15,
        width: 200,
        height:50
    },
    buttonText: {
        color: '#fff',
        fontWeight: 'bold',
        textAlign: "center"
    },
    cardImage: {
        width: 60,
        height: 60,
        resizeMode: 'contain',
    },
});
