package pl.coderslab.cultureBuddies.buddies;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.coderslab.cultureBuddies.exceptions.NonExistingNameException;
import pl.coderslab.cultureBuddies.security.RoleRepository;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BuddyServiceImpl implements BuddyService {
    private final BuddyRepository buddyRepository;
    private final PictureService pictureService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public boolean save(MultipartFile profilePicture, Buddy buddy) throws IOException {
        if (isDuplicate(buddy)) {
            log.warn("Not unique username: {}", buddy.getUsername());
            return false;
        }
        prepareBuddy(profilePicture, buddy);
        final Buddy savedBuddy = buddyRepository.save(buddy);
        log.debug("Entity {} has been saved", savedBuddy);
        return true;
    }

    @Override
    public Buddy findByUsername(String username) throws NonExistingNameException {
        return buddyRepository.findFirstByUsernameIgnoringCase(username)
                .orElseThrow(new NonExistingNameException("Buddy with username" + username + " does not exist in database"));
    }

    private void prepareBuddy(MultipartFile profilePicture, Buddy buddy) throws IOException {
        pictureService.save(profilePicture, buddy);
        buddy.setPassword(passwordEncoder.encode(buddy.getPassword()));
        buddy.addRole(roleRepository.findFirstByNameIgnoringCase("ROLE_USER"));
    }

    private boolean isDuplicate(Buddy buddy) {
        final Optional<Buddy> existingBuddyDb = buddyRepository.findFirstByUsernameIgnoringCase(buddy.getUsername());
        return existingBuddyDb.isPresent();
    }
}