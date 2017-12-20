package ru.nsu.fit.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.nsu.fit.database.entities.File;
import ru.nsu.fit.database.entities.Folder;
import ru.nsu.fit.database.entities.User;
import ru.nsu.fit.database.repositories.FileDataRepository;
import ru.nsu.fit.database.repositories.FileRepository;
import ru.nsu.fit.database.repositories.FolderRepository;
import ru.nsu.fit.database.repositories.UserRepository;
import ru.nsu.fit.database.types.Role;
import ru.nsu.fit.mock.MockFileDataRepository;
import ru.nsu.fit.mock.MockFileRepository;
import ru.nsu.fit.mock.MockFolderRepository;
import ru.nsu.fit.mock.MockUserRepository;
import ru.nsu.fit.web.navigation.FileDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class NavigationServiceTest {
    private FolderRepository folderRepository = new MockFolderRepository();
    private FileRepository fileRepository = new MockFileRepository();
    private UserRepository userRepository = new MockUserRepository();
    private FileDataRepository fileDataRepository = new MockFileDataRepository();
    private NavigationService navigationService = new NavigationService(folderRepository, fileRepository, userRepository, fileDataRepository);
    private User user;
    private File file;

    @Before
    public void before() {
        fileRepository.deleteAll();
        userRepository.deleteAll();
        user = userRepository.save(new User("default", "default", Role.USER));
        navigationService.initializeFolders();
        file = navigationService.createFile(new FileDTO(new File("A", user, null, "B", "C", "D", 1, 1, 1)), navigationService.getFolderInfo(navigationService.getRootFolderId()), user);
    }

    @Test
    public void findRoot() throws Exception {
        Assert.assertEquals(navigationService.getRootFolderId(), (int) folderRepository.findByRoot(true).getId());
    }

    @Test
    public void containsFolder() {
        int folder = navigationService.getRootFolderId();
        Assert.assertTrue(navigationService.containsFolder(folder));
        Assert.assertFalse(navigationService.containsFolder(folder + 1));
    }

    @Test
    public void getFolderInfo() throws Exception {
        Folder folder = navigationService.getFolderInfo(navigationService.getRootFolderId());
        Assert.assertEquals(folder.getName(), "root");
    }

    @Test
    public void createFile() throws Exception {
        file = navigationService.createFile(new FileDTO(new File("B", user, null, "Test", "C", "D", 1, 1, 1)), navigationService.getFolderInfo(navigationService.getRootFolderId()), user);
        Assert.assertTrue(navigationService.containsFile(file.getId()));
        Assert.assertEquals(file.getName(), "B");
        Assert.assertNotNull(file.getId());
    }

    @Test
    public void getAllFiles() throws Exception {
        Assert.assertEquals(navigationService.getAllFiles().size(), 1);
        Assert.assertEquals(navigationService.getAllFiles().get(0).getName(), file.getName());
    }

    @Test
    public void getFilesOfUser() throws Exception {
        Assert.assertEquals(navigationService.getFilesOfUser(user.getId()).size(), 1);
    }

    @Test
    public void fileInfo() throws Exception {
        Assert.assertEquals(navigationService.getFileInfo(file.getId()).getName(), file.getName());
    }

    @Test
    public void searchFiles() throws Exception {
        List<File> files = navigationService.searchFiles("default");
        Assert.assertEquals(files.size(), 1);

        files = navigationService.searchFiles("A");
        Assert.assertEquals(files.size(), 1);

        files = navigationService.searchFiles("-");
        Assert.assertEquals(files.size(), 0);

        files = navigationService.searchFiles("B");
        Assert.assertEquals(files.size(), 1);
    }

    @Test
    public void createPath() throws Exception {
        Folder f = navigationService.createPath(Collections.singletonList("F1"), user);
        Assert.assertTrue(navigationService.containsFolder(f.getId()));
    }


}
