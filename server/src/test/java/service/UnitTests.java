package service;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import passoff.model.TestUser;
import passoff.server.TestServerFacade;

import server.Server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UnitTests {
    private static Server server;
    private static TestServerFacade serverFacade;
    private static TestUser newUser;

    @BeforeAll
    public static void startServer() {
        var port = new Server().run(0);
        serverFacade = new TestServerFacade("localhost", Integer.toString(port));

        newUser = new TestUser("JAWILL", "password", "jawill@byu.edu");
        newLogin = new
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

//    @BeforeEach
//    void clear() {
//        assertDoesNotThrow(() -> serverFacade.clear());
//    }

    @Test
    void Register() {
        var passwordResult = assertDoesNotThrow(() -> serverFacade.register(newUser));
        assertEquals("JAWILL", passwordResult.getUsername());

        var rentry = assertDoesNotThrow(() -> serverFacade.register(newUser));
        assertEquals("Error: already taken", rentry.getMessage());
    }

    @Test
    void Login() {
        newUser
        var expected = new ArrayList<Pet>();
        expected.add(server.addPet(new Pet(0, "sally", PetType.CAT)));

        var joe = server.addPet(new Pet(0, "joe", PetType.CAT));
        server.deletePet(joe.id());

        var result = assertDoesNotThrow(() -> server.listPets());
        assertPetCollectionEqual(expected, List.of(result));
    }

    @Test
    void listPets() throws Exception {
        var expected = new ArrayList<Pet>();
        expected.add(server.addPet(new Pet(0, "joe", PetType.CAT)));
        expected.add(server.addPet(new Pet(0, "sally", PetType.CAT)));

        var result = assertDoesNotThrow(() -> server.listPets());
        assertPetCollectionEqual(expected, List.of(result));
    }

    private void assertPetEqual(Pet expected, Pet actual) {
        assertEquals(expected.name(), actual.name());
        assertEquals(expected.type(), actual.type());
    }


    private void assertPetCollectionEqual(Collection<Pet> expected, Collection<Pet> actual) {
        Pet[] actualList = actual.toArray(new Pet[]{});
        Pet[] expectedList = expected.toArray(new Pet[]{});
        assertEquals(expectedList.length, actualList.length);
        for (var i = 0; i < actualList.length; i++) {
            assertPetEqual(expectedList[i], actualList[i]);
        }
    }

    public class CustomServerFacade {
        private final Server server;

        public CustomServerFacade(Server port) {
            this.server = new Server();
        }

        public void stop() {
            server.stop();
        }

        public void clear() {
            server.clear();
        }

        public TestAuthResult register(TestUser user) {
            // Call the appropriate server method and return the result
            // in the expected format for TestAuthResult
        }

        public TestAuthResult login(TestUser user) {
            // Call the appropriate server method and return the result
            // in the expected format for TestAuthResult
        }

        public TestResult logout(String authToken) {
            // Call the appropriate server method and return the result
            // in the expected format for TestResult
        }

        public TestCreateResult createGame(TestCreateRequest request, String authToken) {
            // Call the appropriate server method and return the result
            // in the expected format for TestCreateResult
        }

        public TestResult joinPlayer(TestJoinRequest request, String authToken) {
            // Call the appropriate server method and return the result
            // in the expected format for TestResult
        }

        public TestListResult listGames(String authToken) {
            // Call the appropriate server method and return the result
            // in the expected format for TestListResult
        }

        // Add any other methods you need to interact with the Server instance
    }

}