package RequestResponse;
import model.GameData;

import java.util.List;

public record GetAllGamesResponse(List<GameData> games) { }
