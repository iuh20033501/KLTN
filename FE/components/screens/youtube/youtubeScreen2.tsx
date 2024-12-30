import React, { useRef, useState } from 'react';
import { View, StyleSheet, FlatList, Text, TouchableOpacity } from 'react-native';
import { Video, ResizeMode, VideoFullscreenUpdateEvent } from 'expo-av';
import { Ionicons } from '@expo/vector-icons';
import * as ScreenOrientation from 'expo-screen-orientation';

const videoList = [
  {
    id: '1',
    title: 'Vocabulary 1',
    uri: 'https://kltn-iuh-20033501.s3.ap-southeast-1.amazonaws.com/video/vocabulary1.mp4',
  },
  {
    id: '2',
    title: 'Vocabulary 2',
    uri: 'https://kltn-iuh-20033501.s3.ap-southeast-1.amazonaws.com/video/vocabulary2.mp4',
  },
  {
    id: '3',
    title: 'Vocabulary 3',
    uri: 'https://kltn-iuh-20033501.s3.ap-southeast-1.amazonaws.com/video/vocabulary3.mp4',
  },
  {
    id: '4',
    title: 'Vocabulary 4',
    uri: 'https://kltn-iuh-20033501.s3.ap-southeast-1.amazonaws.com/video/vocabulary4.mp4',
  },
  {
    id: '5',
    title: 'Vocabulary 5',
    uri: 'https://kltn-iuh-20033501.s3.ap-southeast-1.amazonaws.com/video/vocabulary5.mp4',
  },
  
];

export default function YouTubeScreen2({ navigation }: { navigation: any }) {
  const videoRef = useRef<Video>(null);
  const [currentVideo, setCurrentVideo] = useState(videoList[0]);
  const [isPlaying, setIsPlaying] = useState(false);

  const togglePlayback = async () => {
    if (videoRef.current) {
      if (isPlaying) {
        await videoRef.current.pauseAsync();
        setIsPlaying(false);
      } else {
        await videoRef.current.playAsync();
        setIsPlaying(true);
      }
    }
  };

  const toggleFullScreen = async () => {
    if (videoRef.current) {
      await ScreenOrientation.lockAsync(ScreenOrientation.OrientationLock.LANDSCAPE);
      await videoRef.current.presentFullscreenPlayer();
    }
  };

  const handleFullscreenUpdate = async (event: VideoFullscreenUpdateEvent) => {
    if (event.fullscreenUpdate === 3) {
      await ScreenOrientation.lockAsync(ScreenOrientation.OrientationLock.PORTRAIT);
    }
  };

  const selectVideo = (video: React.SetStateAction<{ id: string; title: string; uri: string }>) => {
    setCurrentVideo(video);
    setIsPlaying(false);
    if (videoRef.current) {
      videoRef.current.stopAsync();
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={() => navigation.goBack()} style={styles.goBackButton}>
          <Ionicons name="arrow-back" size={24} color="#000" />
        </TouchableOpacity>
        <Text style={styles.title}>Học từ vựng theo chủ đề</Text>
      </View>

      <Text style={styles.titleVideo}>{currentVideo.title}</Text>

      <Video
        ref={videoRef}
        source={{ uri: currentVideo.uri }}
        rate={1.0}
        volume={1.0}
        isMuted={false}
        resizeMode={ResizeMode.COVER}
        shouldPlay={false}
        style={styles.videoPlayer}
        onFullscreenUpdate={handleFullscreenUpdate} 
      />

      <View style={styles.buttonContainer}>
        <TouchableOpacity style={styles.button} onPress={togglePlayback}>
          <Text style={styles.buttonText}>{isPlaying ? 'Pause' : 'Play'}</Text>
        </TouchableOpacity>

        <TouchableOpacity style={styles.button} onPress={toggleFullScreen}>
          <Text style={styles.buttonText}>Fullscreen</Text>
        </TouchableOpacity>
      </View>

      <FlatList
        data={videoList}
        keyExtractor={(item) => item.id}
        renderItem={({ item }) => (
          <TouchableOpacity style={styles.videoItem} onPress={() => selectVideo(item)}>
            <Text style={styles.videoTitle}>{item.title}</Text>
          </TouchableOpacity>
        )}
        style={styles.videoList}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
    backgroundColor: '#fff',
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 16,
  },
  goBackButton: {
    marginRight: 8,
  },
  title: {
    fontSize: 18,
    fontWeight: 'bold',
    textAlign: 'center',
    flex: 1,
    marginRight: 24,
  },
  titleVideo: {
    fontSize: 16,
    fontWeight: 'bold',
    textAlign: 'left',
    marginBottom: 16,
  },
  videoPlayer: {
    width: '100%',
    height: 200,
    backgroundColor: '#000',
    marginBottom: 16,
  },
  buttonContainer: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    marginBottom: 16,
  },
  button: {
    backgroundColor: '#00bf63',
    paddingVertical: 10,
    paddingHorizontal: 20,
    borderRadius: 8,
  },
  buttonText: {
    color: '#fff',
    fontWeight: 'bold',
  },
  videoList: {
    marginTop: 16,
  },
  videoItem: {
    padding: 12,
    marginBottom: 8,
    backgroundColor: '#f9f9f9',
    borderRadius: 8,
    borderWidth: 1,
    borderColor: '#ddd',
  },
  videoTitle: {
    fontSize: 16,
    fontWeight: 'bold',
  },
});
