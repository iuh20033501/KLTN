import React, { useState, useEffect } from 'react';
import { View, TouchableOpacity, Text, StyleSheet } from 'react-native';
import { Audio } from 'expo-av';
import Slider from '@react-native-community/slider';
import AntDesign from '@expo/vector-icons/AntDesign';

interface AudioPlayerProps {
    audioUri: string;
}

const AudioPlayer: React.FC<AudioPlayerProps> = ({ audioUri }) => {
    const [sound, setSound] = useState<Audio.Sound | null>(null);
    const [isPlaying, setIsPlaying] = useState(false);
    const [progress, setProgress] = useState(0);
    const [duration, setDuration] = useState(1);
    const [volume, setVolume] = useState(1);

    useEffect(() => {
        const loadAudio = async () => {
            try {
                const { sound } = await Audio.Sound.createAsync(
                    { uri: audioUri },
                    { shouldPlay: false },
                    onPlaybackStatusUpdate
                );
                setSound(sound);

                const status = await sound.getStatusAsync();
                if (status.isLoaded && status.durationMillis) {
                    setDuration(status.durationMillis);
                } else {
                    console.warn('Unable to retrieve duration, using fallback value.');
                    setDuration(60000);
                }
                await sound.setVolumeAsync(volume);
            } catch (error) {
                console.error('Error loading audio:', error);
                setDuration(60000);
            }
        };

        loadAudio();

        return () => {
            sound?.unloadAsync();
        };
    }, [audioUri]);

    const onPlaybackStatusUpdate = (status: any) => {
        if (status.isLoaded) {
            setProgress(status.positionMillis || 0);

            if (status.didJustFinish) {
                setIsPlaying(false);
                setProgress(0);
            }
        }
    };

    const handlePlayPause = async () => {
        if (sound) {
            const status = await sound.getStatusAsync();
            if (status.isLoaded) {
                if (status.didJustFinish || status.positionMillis >= (status.durationMillis || 0)) {
                    await sound.setPositionAsync(0); // Tua về đầu nếu âm thanh kết thúc
                }

                if (isPlaying) {
                    await sound.pauseAsync(); // Dừng phát
                } else {
                    await sound.playAsync(); // Phát lại
                }

                setIsPlaying(!isPlaying); // Cập nhật trạng thái phát
            } else {
                console.error('Sound is not loaded correctly.');
            }
        }
    };

    const handleSeek = async (value: number) => {
        if (sound) {
            try {
                await sound.setPositionAsync(value);
                setProgress(value);
            } catch (error) {
                console.error('Error seeking audio:', error);
            }
        }
    };

    const handleVolumeChange = async (value: number) => {
        const normalizedVolume = value / 100;
        setVolume(normalizedVolume);
        if (sound) {
            await sound.setVolumeAsync(normalizedVolume);
        }
    };

    const formatTime = (millis: number) => {
        const minutes = Math.floor(millis / 60000);
        const seconds = Math.floor((millis % 60000) / 1000);
        return `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
    };

    return (
        <View style={styles.container}>
            <Slider
                style={{ width: 300, height: 20 }}
                minimumValue={0}
                maximumValue={duration || 1}
                value={progress}
                onSlidingComplete={(value) => handleSeek(value)}
                minimumTrackTintColor="#1FB28A"
                maximumTrackTintColor="#D3D3D3"
                thumbTintColor="#1FB28A"
            />
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', width: 280 }}>
                <Text>{formatTime(progress)}</Text>
                <Text>{formatTime(duration)}</Text>
            </View>
            <View style={styles.volumeControl}>
                <View style={{ flexDirection: 'row' }}>
                    <AntDesign name="sound" size={24} color="#1DB954" />
                    <Slider
                        style={styles.volumeSlider}
                        minimumValue={0}
                        maximumValue={100}
                        value={volume * 100}
                        onValueChange={(value) => handleVolumeChange(value)}
                        minimumTrackTintColor="#1FB28A"
                        maximumTrackTintColor="#D3D3D3"
                        thumbTintColor="#1FB28A"
                    />
                </View>
            </View>

            <View style={styles.controls}>
                <TouchableOpacity onPress={handlePlayPause} style={styles.playPauseButton}>
                    <Text style={styles.playPauseButtonText}>{isPlaying ? 'Tạm dừng' : 'Phát'}</Text>
                </TouchableOpacity>
            </View>
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        marginVertical: 10,
        alignItems: 'center',
    },
    clearButton: {
        position: 'absolute',
        top: -10,
        right: -10,
        zIndex: 1,
    },
    slider: {
        width: '90%',
        height: 20,
    },
    controls: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        marginTop: -10,
    },
    playPauseButton: {
        backgroundColor: '#1DB954',
        paddingHorizontal: 20,
        paddingVertical: 10,
        borderRadius: 10,
        marginTop:10
    },
    playPauseButtonText: {
        color: '#fff',
        fontWeight: 'bold',
    },
    volumeControl: {
        alignItems: 'center',
        top: -10,
    },
    volumeSlider: {
        width: 100,
        height: 20,
        marginRight: 10,
        marginTop: 2,
    },
});

export default AudioPlayer;
