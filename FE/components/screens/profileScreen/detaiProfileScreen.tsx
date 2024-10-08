import React, { useState } from 'react';
import { View, Text, StyleSheet, TextInput, Image, Switch, TouchableOpacity } from 'react-native';
import { useNavigation } from '@react-navigation/native';

export default function DetailProfileScreen({navigation}: {navigation: any}) {
  const [name, setName] = useState('khò khò');
  const [username, setUsername] = useState('halequyenn');
  const [pronouns, setPronouns] = useState('');
  const [bio, setBio] = useState('');
  const [gender, setGender] = useState('Male');
  const [showThreadsBadge, setShowThreadsBadge] = useState(false);

  return (
    <View style={styles.container}>
      <TouchableOpacity onPress={() => navigation.goBack()} style={styles.backButton}>
        <Text style={styles.backButtonText}>←</Text>
      </TouchableOpacity>
      
      <Text style={styles.title}>Edit profile</Text>

      <View style={styles.avatarSection}>
        <Image source={require('../../../image/avatar/1.png')} style={styles.avatar} />
        <Text style={styles.editAvatarText}>Edit picture or avatar</Text>
      </View>

      <View style={styles.formSection}>
        <TextInput
          style={styles.input}
          value={name}
          onChangeText={setName}
          placeholder="Name"
        />

        <TextInput
          style={styles.input}
          value={username}
          onChangeText={setUsername}
          placeholder="Username"
        />

        <TextInput
          style={styles.input}
          value={pronouns}
          onChangeText={setPronouns}
          placeholder="Pronouns"
        />

        <TextInput
          style={styles.input}
          value={bio}
          onChangeText={setBio}
          placeholder="Bio"
          multiline
        />

        <View style={styles.inputRow}>
          <Text style={styles.label}>Gender</Text>
          <TouchableOpacity onPress={() => setGender(gender === 'Male' ? 'Female' : 'Male')}>
            <Text style={styles.dropdown}>{gender}</Text>
          </TouchableOpacity>
        </View>

        <View style={styles.inputRow}>
          <Text style={styles.label}>Show Threads badge</Text>
          <Switch
            value={showThreadsBadge}
            onValueChange={setShowThreadsBadge}
          />
        </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#121212',
    padding: 20,
  },
  backButton: {
    marginBottom: 10,
  },
  backButtonText: {
    fontSize: 24,
    color: '#fff',
  },
  title: {
    fontSize: 22,
    color: '#fff',
    textAlign: 'center',
    fontWeight: 'bold',
    marginBottom: 20,
  },
  avatarSection: {
    alignItems: 'center',
    marginBottom: 20,
  },
  avatar: {
    width: 80,
    height: 80,
    borderRadius: 40,
    marginBottom: 10,
  },
  editAvatarText: {
    color: '#1DA1F2',
    fontSize: 14,
  },
  formSection: {
    marginBottom: 20,
  },
  input: {
    backgroundColor: '#1f1f1f',
    borderRadius: 10,
    padding: 10,
    marginBottom: 10,
    color: '#fff',
  },
  inputRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10,
  },
  label: {
    fontSize: 16,
    color: '#fff',
  },
  dropdown: {
    fontSize: 16,
    color: '#fff',
    backgroundColor: '#1f1f1f',
    borderRadius: 10,
    padding: 10,
  },
});
