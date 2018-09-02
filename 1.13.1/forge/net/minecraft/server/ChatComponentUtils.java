package net.minecraft.server;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Nullable;

public class ChatComponentUtils {
    public static IChatBaseComponent a(IChatBaseComponent ichatbasecomponent, ChatModifier chatmodifier) {
        if (chatmodifier.g()) {
            return ichatbasecomponent;
        } else {
            return ichatbasecomponent.getChatModifier().g() ? ichatbasecomponent.setChatModifier(chatmodifier.clone()) : (new ChatComponentText("")).addSibling(ichatbasecomponent).setChatModifier(chatmodifier.clone());
        }
    }

    public static IChatBaseComponent filterForDisplay(@Nullable CommandListenerWrapper commandlistenerwrapper, IChatBaseComponent ichatbasecomponent, @Nullable Entity entity) throws CommandSyntaxException {
        Object object;
        if (ichatbasecomponent instanceof ChatComponentScore && commandlistenerwrapper != null) {
            ChatComponentScore chatcomponentscore = (ChatComponentScore)ichatbasecomponent;
            String s;
            if (chatcomponentscore.j() != null) {
                List list = chatcomponentscore.j().b(commandlistenerwrapper);
                if (list.isEmpty()) {
                    s = chatcomponentscore.i();
                } else {
                    if (list.size() != 1) {
                        throw ArgumentEntity.a.create();
                    }

                    s = ((Entity)list.get(0)).getName();
                }
            } else {
                s = chatcomponentscore.i();
            }

            String s1 = entity != null && s.equals("*") ? entity.getName() : s;
            object = new ChatComponentScore(s1, chatcomponentscore.k());
            ((ChatComponentScore)object).b(chatcomponentscore.getText());
            ((ChatComponentScore)object).b(commandlistenerwrapper);
        } else if (ichatbasecomponent instanceof ChatComponentSelector && commandlistenerwrapper != null) {
            object = ((ChatComponentSelector)ichatbasecomponent).a(commandlistenerwrapper);
        } else if (ichatbasecomponent instanceof ChatComponentText) {
            object = new ChatComponentText(((ChatComponentText)ichatbasecomponent).i());
        } else if (ichatbasecomponent instanceof ChatComponentKeybind) {
            object = new ChatComponentKeybind(((ChatComponentKeybind)ichatbasecomponent).j());
        } else {
            if (!(ichatbasecomponent instanceof ChatMessage)) {
                return ichatbasecomponent;
            }

            Object[] aobject = ((ChatMessage)ichatbasecomponent).l();

            for(int i = 0; i < aobject.length; ++i) {
                Object object1 = aobject[i];
                if (object1 instanceof IChatBaseComponent) {
                    aobject[i] = filterForDisplay(commandlistenerwrapper, (IChatBaseComponent)object1, entity);
                }
            }

            object = new ChatMessage(((ChatMessage)ichatbasecomponent).k(), aobject);
        }

        for(IChatBaseComponent ichatbasecomponent1 : ichatbasecomponent.a()) {
            ((IChatBaseComponent)object).addSibling(filterForDisplay(commandlistenerwrapper, ichatbasecomponent1, entity));
        }

        return a((IChatBaseComponent)object, ichatbasecomponent.getChatModifier());
    }

    public static IChatBaseComponent a(GameProfile gameprofile) {
        if (gameprofile.getName() != null) {
            return new ChatComponentText(gameprofile.getName());
        } else {
            return gameprofile.getId() != null ? new ChatComponentText(gameprofile.getId().toString()) : new ChatComponentText("(unknown)");
        }
    }

    public static IChatBaseComponent a(Collection<String> collection) {
        return a(collection, (s) -> {
            return (new ChatComponentText(s)).a(EnumChatFormat.GREEN);
        });
    }

    public static <T extends Comparable<T>> IChatBaseComponent a(Collection<T> collection, Function<T, IChatBaseComponent> function) {
        if (collection.isEmpty()) {
            return new ChatComponentText("");
        } else if (collection.size() == 1) {
            return (IChatBaseComponent)function.apply(collection.iterator().next());
        } else {
            ArrayList arraylist = Lists.newArrayList(collection);
            arraylist.sort(Comparable::compareTo);
            return b(collection, function);
        }
    }

    public static <T> IChatBaseComponent b(Collection<T> collection, Function<T, IChatBaseComponent> function) {
        if (collection.isEmpty()) {
            return new ChatComponentText("");
        } else if (collection.size() == 1) {
            return (IChatBaseComponent)function.apply(collection.iterator().next());
        } else {
            ChatComponentText chatcomponenttext = new ChatComponentText("");
            boolean flag = true;

            for(Object object : collection) {
                if (!flag) {
                    chatcomponenttext.addSibling((new ChatComponentText(", ")).a(EnumChatFormat.GRAY));
                }

                chatcomponenttext.addSibling((IChatBaseComponent)function.apply(object));
                flag = false;
            }

            return chatcomponenttext;
        }
    }

    public static IChatBaseComponent a(IChatBaseComponent ichatbasecomponent) {
        return (new ChatComponentText("[")).addSibling(ichatbasecomponent).a("]");
    }

    public static IChatBaseComponent a(Message message) {
        return (IChatBaseComponent)(message instanceof IChatBaseComponent ? (IChatBaseComponent)message : new ChatComponentText(message.getString()));
    }
}